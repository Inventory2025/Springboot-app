package com.ims.inventory.service.impl;

import com.ims.inventory.constants.ImsConstants;
import com.ims.inventory.domen.entity.BMCompDetail;
import com.ims.inventory.domen.entity.BMCompElements;
import com.ims.inventory.domen.request.*;
import com.ims.inventory.domen.response.FilterResponse;
import com.ims.inventory.domen.response.MenuResponse;
import com.ims.inventory.domen.response.Responce;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.exception.ResourceNotFoundException;
import com.ims.inventory.helpers.ComponentHelper;
import com.ims.inventory.helpers.CsvExportHelper;
import com.ims.inventory.service.JdbcTemplateService;
import com.ims.inventory.service.ModuleService;
import com.ims.inventory.utility.JsonUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ComponentHelper componentHelper;

    @Autowired
    private JdbcTemplateService jdbcTemplateService;

    @Autowired
    private CsvExportHelper csvExportHelper;

    private final String success = "success";
    private final String dataNotFound = "Data not found";
    private final String EMPTY_SPACE = " ";

    public FilterResponse filterlist(FilterRequest filterRequest) throws Exception {
        if (filterRequest != null && filterRequest.getModule() != null) {
           // String selectQuery = componentHelper.getSelectQueryByBMCompCode(filterRequest.getModule());
            BMCompDetail bmCompDetail = componentHelper.getBMCompDetail(filterRequest.getModule());
            if (bmCompDetail != null && !ObjectUtils.isEmpty(bmCompDetail.getSelectQuery())) {
               /* FilterResponse filterResponse = jdbcTemplateService.getQueryResult(filterRequest.getPageNo()
                        , filterRequest.getPageSize(), bmCompDetail.getSelectQuery());*/

                FilterResponse filterResponse = buildFilterQuery(filterRequest, bmCompDetail.getSelectQuery());
                filterResponse.setLabel(bmCompDetail.getBmComponent().getLabel());
                filterResponse.setSubModuleLink(bmCompDetail.getBmComponent().getSbcLink());
                return filterResponse;
            } else {
                throw new Exception("Select Query Data Not Found");
            }
        } else {
            throw new Exception("Input data is null.");
        }
    }

    private FilterResponse buildFilterQuery(FilterRequest filterRequest, String selectQuery) throws Exception {

        if (!ObjectUtils.isEmpty(selectQuery)) {
            StringBuffer sql = new StringBuffer();
            StringBuffer countSql = new StringBuffer();
            sql.append("select * from ( ").append(selectQuery).append(" ) as t");
            MapSqlParameterSource params = new MapSqlParameterSource();
            if (ObjectUtils.isNotEmpty(filterRequest.getFilters())) {
                int i = 0;
                sql.append(" where ");
                for (Filter filter : filterRequest.getFilters()) {
                    if (filter.getValues() == null || filter.getValues().isEmpty()) continue;


                    StringBuffer colCondSql = new StringBuffer();
                    colCondSql.append(" ( ");
                    switch (filter.getOperator().toUpperCase()) {

                        case "=":
                        case ">":
                        case "<":
                        case ">=":
                        case "<=":
                            if ("Boolean".equalsIgnoreCase(filter.getDataType())) {
                                buildFilterValueCondForString(colCondSql, filter.getColumn(), "=",
                                        filter.getCondition().toUpperCase(), filter.getValues(), true);
                            } else if ("Date".equalsIgnoreCase(filter.getDataType())) {
                                buildFilterValueCondForDate(colCondSql, filter.getColumn(), filter.getOperator().toUpperCase(),
                                        filter.getCondition().toUpperCase(), filter.getValues());
                            } else {
                                buildFilterValueCond(colCondSql, filter.getColumn(), filter.getOperator().toUpperCase(),
                                        filter.getCondition().toUpperCase(), filter.getValues());
                            }

                            break;

                        case "EQUALS":
                            if ("Date".equalsIgnoreCase(filter.getDataType())) {
                                buildFilterValueCondForDate(colCondSql, filter.getColumn(), "=",
                                        filter.getCondition().toUpperCase(), filter.getValues());
                            } else {
                                buildFilterValueCondForString(colCondSql, filter.getColumn(), "=",
                                        filter.getCondition().toUpperCase(), filter.getValues(), false);
                            }
                            break;

                        case "NOTEQUALS":
                            if ("Date".equalsIgnoreCase(filter.getDataType())) {
                                buildFilterValueCondForDate(colCondSql, filter.getColumn(), "!=",
                                        filter.getCondition().toUpperCase(), filter.getValues());
                            } else {
                                buildFilterValueCondForString(colCondSql, filter.getColumn(), "!=",
                                        filter.getCondition().toUpperCase(), filter.getValues(), false);
                            }
                            break;

                        case "CONTAINS":
                            buildFilterValueCondForLike(colCondSql, filter.getColumn(), filter.getOperator().toUpperCase(),
                                    filter.getCondition().toUpperCase(), filter.getValues());
                            break;

                        case "STARTSWITH":
                            buildFilterValueCondForStartWith(colCondSql, filter.getColumn(), filter.getOperator().toUpperCase(),
                                    filter.getCondition().toUpperCase(), filter.getValues());
                            break;

                        case "ENDSWITH":
                            buildFilterValueCondForEndWith(colCondSql, filter.getColumn(), filter.getOperator().toUpperCase(),
                                    filter.getCondition().toUpperCase(), filter.getValues());
                            break;

                        case "IN":
                            buildFilterValueCondForIn(colCondSql, filter.getColumn(), filter.getOperator().toUpperCase(),
                                    filter.getCondition().toUpperCase(), filter.getValues());
                            break;
                    }
                    colCondSql.append(" ) ");

                    if (i > 0) {
                        sql.append(EMPTY_SPACE).append("AND").append(EMPTY_SPACE);
                    }
                    sql.append(colCondSql);
                    i++;
                }
            }

            countSql.append("select count(1) from ( ").append(sql).append(" ) as t");

            // Add sorting
            if (ObjectUtils.isNotEmpty(filterRequest.getSortBy())
                    && StringUtils.isNotEmpty(filterRequest.getSortBy().getSortBy())) {
                String direction = ("DESC".equalsIgnoreCase(filterRequest.getSortBy().getDirection())) ? "DESC" : "ASC";
                sql.append(" ORDER BY ").append(filterRequest.getSortBy().getSortBy()).append(" ").append(direction);
            }

            // Add pagination
            sql.append(" LIMIT ").append(filterRequest.getPageSize());
            sql.append(" OFFSET ").append(((filterRequest.getPageNo()-1) * filterRequest.getPageSize()));

            FilterResponse filterResponse = jdbcTemplateService.getQueryResultWithFilter(filterRequest.getPageNo()
                    , filterRequest.getPageSize(), sql.toString(), countSql.toString());

            return filterResponse;


        }
        throw new ResourceNotFoundException("Select query is null/Empty for this module :"+filterRequest.getModule());
    }



    private void buildFilterValueCond(StringBuffer colCondSql, String column, String filterOperator,
                                      String valueOperator, List<FilterValue> values) {
        int index = 0;
        for (FilterValue value : values) {
            colCondSql.append("(").append(column).append(EMPTY_SPACE).append(filterOperator).append(EMPTY_SPACE)
                    .append(value.getValue().trim()).append(")");
            if (index > 0) {
                colCondSql.append(EMPTY_SPACE).append(valueOperator).append(EMPTY_SPACE);
            }
            index++;
        }
    }

    private void buildFilterValueCondForDate(StringBuffer colCondSql, String column, String filterOperator,
                                      String valueOperator, List<FilterValue> values) {
        int index = 0;
        for (FilterValue value : values) {
            colCondSql.append("(").append(column).append("::date").append(EMPTY_SPACE).append(filterOperator).append(EMPTY_SPACE)
                    .append("'").append(value.getValue().trim()).append("'").append(")");
            if (index > 0) {
                colCondSql.append(EMPTY_SPACE).append(valueOperator).append(EMPTY_SPACE);
            }
            index++;
        }
    }

    private void buildFilterValueCondForString(StringBuffer colCondSql, String column, String filterOperator,
                                      String valueOperator, List<FilterValue> values, Boolean isBoolean) {
        int index = 0;
        for (FilterValue value : values) {
            String valueStr = value.getValue().toUpperCase();
            colCondSql.append("(").append("upper(").append(column).append(")")
                    .append(EMPTY_SPACE).append(filterOperator).append(EMPTY_SPACE)
                    .append("'").append(valueStr.trim()).append("'").append(")");
            if (index > 0) {
                colCondSql.append(EMPTY_SPACE).append(valueOperator).append(EMPTY_SPACE);
            }
            index++;
        }
    }

    private void buildFilterValueCondForIn(StringBuffer colCondSql, String column, String filterOperator,
                                      String valueOperator, List<FilterValue> values) {
        colCondSql.append("upper(").append(column).append(")").append(EMPTY_SPACE).append("IN").append(EMPTY_SPACE)
                .append("(");
        int index = 0;
        for (FilterValue value : values) {
            if (index > 0) {
                colCondSql.append(EMPTY_SPACE).append(", ");
            }
            colCondSql.append("'").append(value.getValue().trim().toUpperCase()).append("'");
            index++;
        }
        colCondSql.append(")");
    }

    private void buildFilterValueCondForLike(StringBuffer colCondSql, String column, String filterOperator,
                                           String valueOperator, List<FilterValue> values) {
        int index = 0;
        if (StringUtils.isNotEmpty(valueOperator)) {
            for (FilterValue value : values) {
                colCondSql.append("(").append("upper(").append(column).append(")")
                        .append(EMPTY_SPACE).append("LIKE").append(EMPTY_SPACE)
                        .append("'%").append(value.getValue().trim().toUpperCase()).append("%'").append(")");
                if (index > 0) {
                    colCondSql.append(EMPTY_SPACE).append(valueOperator).append(EMPTY_SPACE);
                }
                index++;
            }
        }
    }

    private void buildFilterValueCondForStartWith(StringBuffer colCondSql, String column, String filterOperator,
                                             String valueOperator, List<FilterValue> values) {
        int index = 0;
        if (StringUtils.isNotEmpty(valueOperator)) {
            for (FilterValue value : values) {
                colCondSql.append("(").append("upper(").append(column).append(")")
                        .append(EMPTY_SPACE).append("LIKE").append(EMPTY_SPACE)
                        .append("'").append(value.getValue().trim().toUpperCase()).append("%'").append(")");
                if (index > 0) {
                    colCondSql.append(EMPTY_SPACE).append(valueOperator).append(EMPTY_SPACE);
                }
                index++;
            }
        }
    }

    private void buildFilterValueCondForEndWith(StringBuffer colCondSql, String column, String filterOperator,
                                                  String valueOperator, List<FilterValue> values) {
        int index = 0;
        if (StringUtils.isNotEmpty(valueOperator)) {
            for (FilterValue value : values) {
                colCondSql.append("(").append("upper(").append(column).append(")")
                        .append(EMPTY_SPACE).append("LIKE").append(EMPTY_SPACE)
                        .append("'%").append(value.getValue().trim().toUpperCase()).append("'").append(")");
                if (index > 0) {
                    colCondSql.append(EMPTY_SPACE).append(valueOperator).append(EMPTY_SPACE);
                }
                index++;
            }
        }
    }




    public Responce createModule(CreateRequest createRequest) throws Exception {
        if (createRequest != null && createRequest.getModule() != null) {
            BMCompDetail bmCompDetail = componentHelper.getBMCompDetail(createRequest.getModule());
            if (ObjectUtils.isNotEmpty(bmCompDetail) && ObjectUtils.isNotEmpty(createRequest.getData())) {
                Map<String, Object> inputMap = JsonUtility.jsonStrToMap(createRequest.getData());
                if (bmCompDetail.isCustomLogicEnabled()) {
                  /*  BusinessLogic businessLogic = businessLogicFactory.getBusinessLogic(
                            bmCompDetail.getBmComponent().getCode());
                    if (businessLogic != null) {
                        inputMap = businessLogic.executeBusinessLogic(inputMap);
                    }*/
                }
                if(ObjectUtils.isEmpty(createRequest.getRecordCode())) {
                    return createQuery(bmCompDetail, inputMap);
                } else {
                    return updateQuery(bmCompDetail, inputMap, createRequest.getRecordCode());
                }
            }
        }
        throw new ResourceNotFoundException("Config or Input Data not Found.");
    }

    public Boolean updateModuleDesign(CreateRequest createRequest) throws Exception {
        if (createRequest != null && createRequest.getModule() != null) {
            componentHelper.updateDesignByBMCompCode(createRequest.getModule(),
                    createRequest.getData());
            return true;
        } else {
            throw new Exception("Input data is null.");
        }
    }

    private Responce createQuery(BMCompDetail bmCompDetail, Map<String, Object> inputMap) throws Exception {
        if (!ObjectUtils.isEmpty(bmCompDetail.getInsertQuery()) && null != bmCompDetail.getBmCompElementsList()) {
            String insertQuery = bmCompDetail.getInsertQuery();
            Map<String, Object> queryParams = setCreateQueryParam(bmCompDetail.getBmCompElementsList(), inputMap);
            int result = jdbcTemplateService.execute(insertQuery, queryParams);
            if (result > 0) {
                return new Responce(200L, "Success", result);
            } else {
                throw new Exception("Record insertion failed.");
            }
        }
        throw new ResourceNotFoundException("Config detail not found.");
    }

    private Map<String, Object> setCreateQueryParam(List<BMCompElements> bmCompElementsList,
                                                    Map<String, Object> inputMap) {
        if (ObjectUtils.isNotEmpty(inputMap)) {
            Map<String, Object> queryParams = new HashMap<>();
            for (BMCompElements obj: bmCompElementsList) {
                if (obj.getIsForm()) {
                    queryParams.put(obj.getCode(), inputMap.getOrDefault(obj.getCode(), null));
                }
            }
            return queryParams;
        }
        throw new ResourceNotFoundException("Input Data Map not found.");
    }

    private Responce updateQuery(BMCompDetail bmCompDetail, Map<String, Object> inputMap, String recordCode)
            throws Exception {
        if (null != bmCompDetail.getBmCompElementsList()) {
            String queryParamData = setUpdateQueryParam(bmCompDetail.getBmCompElementsList(), inputMap);
            int result = jdbcTemplateService.executeUpdateWithWhereQuery(bmCompDetail.getTableName(),
                    queryParamData, recordCode);
            if (result > 0) {
                return new Responce(200L, "Success", result);
            } else {
                throw new Exception("Insert Record Failed.");
            }
        }
        throw new ResourceNotFoundException("Config detail not found.");
    }

    private String setUpdateQueryParam(List<BMCompElements> bmCompElementsList,
                                       Map<String, Object> inputMap) {
        if (ObjectUtils.isNotEmpty(inputMap)) {
            StringBuffer sb = new StringBuffer();
            for (BMCompElements obj: bmCompElementsList) {
                if(obj.getIsEditable()) {
                    if (sb.isEmpty()) {
                        sb.append(obj.getColLabel()).append(" = '")
                                .append(inputMap.getOrDefault(obj.getCode(), null)).append("'");
                    } else {
                        sb.append(", ").append(obj.getColLabel()).append(" = '")
                                .append(inputMap.getOrDefault(obj.getCode(), null)).append("'");
                    }
                }
            }
            return sb.toString();
        }
        throw new ResourceNotFoundException("Update Input Data Map not found.");
    }

    public String getTableDesign(CreateRequest createRequest) throws Exception {
        if (createRequest != null && !createRequest.getModule().isEmpty()) {
            return componentHelper.getTableDesignByBMCompCode(createRequest.getModule());
        } else {
            throw new Exception("Input data is null.");
        }
    }

    public String getFormDesign(CreateRequest createRequest) throws Exception {
        if (createRequest != null && !createRequest.getModule().isEmpty()) {
            return componentHelper.getDesignByBMCompCode(createRequest.getModule(), createRequest.getRecordCode());
        } else {
            throw new Exception("Input data is null.");
        }
    }

    public List<MenuResponse> getModuleMenu(HttpServletRequest request) throws Exception {
        try {
            String roleId = (String) request.getAttribute(ImsConstants.ROLE_ID);
            return componentHelper.getModuleMenu(roleId);
        } catch (Exception e) {
            log.error("Exception occurs in getting menu.");
            throw e;
        }
    }

    public FilterResponse getDropDownOptions(DropDownRequest req) throws Exception {
        try {
            if (null != req) {
                return componentHelper.getDropDownOptions(req.getModule(),
                        req.getColumn());
            } else {
                throw new Exception("Request will be null");
            }
        } catch (Exception e) {
            log.error("Exception occurs in getting dropDownResponse.");
            throw e;
        }
    }

    public Responce loadModule(LoadRequest loadRequest) throws Exception {
        try {
            Responce resp = new Responce();
            Map<String, Object> list = componentHelper.loadRecordByBMCompCodeAndRecordCode(
                    loadRequest.getModule(), loadRequest.getRecordCode());
            if (!ObjectUtils.isNotEmpty(list)) {
                resp.setCode(200L);
                resp.setMessage(success);
                resp.setResult(list);
                return resp;
            } else {
                throw new Exception(dataNotFound);
            }
        } catch (Exception e) {
            log.error("Exception occurs in getting loadModule.");
            throw e;
        }
    }

    public Responce softDelete(LoadRequest loadRequest) throws Exception {
        try {
            if (loadRequest.getModule() != null) {
                BMCompDetail bmCompDetail = componentHelper.getBMCompDetail(loadRequest.getModule());
                String tableName = bmCompDetail.getTableName();
                if (ObjectUtils.isNotEmpty(tableName)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("update").append(" ").append(tableName).append(" ");
                    sb.append("set is_active = false where id = '").append(loadRequest.getRecordCode()).append("'");
                    String updateQuery = sb.toString();
                    log.info("ModuleServiceImpl :: softDelete :: Update query -->:"+updateQuery);
                    int flag = jdbcTemplateService.execute(updateQuery);
                    Responce resp = new Responce();
                    resp.setCode(200L);
                    resp.setMessage("Delete successfully.");
                    resp.setResult(loadRequest.getRecordCode());
                    return resp;
                } else {
                    throw new Exception("Input model table not found.");
                }
            } else {
                throw new Exception("Input data is null.");
            }
        } catch (Exception e) {
            log.error("Exception occurs in softDelete.");
            throw e;
        }
    }

    @Override
    public void exportCsv(FilterRequest filterRequest, HttpServletRequest request,
                          HttpServletResponse response) throws Exception, IOException {
        if (filterRequest != null && filterRequest.getModule() != null) {
            BMCompDetail bmCompDetail = componentHelper.getBMCompDetail(filterRequest.getModule());
            if (bmCompDetail != null && !ObjectUtils.isEmpty(bmCompDetail.getSelectQuery())) {
                List<Map<String, Object>> list = jdbcTemplateService.getQueryResult(bmCompDetail.getSelectQuery());
                csvExportHelper.exportCsv(list, response);
            } else {
                throw new Exception("Select Query Data Not Found");
            }
        } else {
            throw new Exception("Input data is null.");
        }
    }
}

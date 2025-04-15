package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.BMCompDetail;
import com.ims.inventory.domen.entity.BMCompElements;
import com.ims.inventory.domen.request.CreateRequest;
import com.ims.inventory.domen.request.DropDownRequest;
import com.ims.inventory.domen.request.FilterRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.response.FilterResponse;
import com.ims.inventory.domen.response.MenuResponse;
import com.ims.inventory.domen.response.Responce;
import com.ims.inventory.exception.ResourceNotFoundException;
import com.ims.inventory.helpers.ComponentHelper;
import com.ims.inventory.service.JdbcTemplateService;
import com.ims.inventory.service.ModuleService;
import com.ims.inventory.utility.JsonUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private final String success = "success";
    private final String dataNotFound = "Data not found";

    public FilterResponse filterlist(FilterRequest filterRequest) throws Exception {
        if (filterRequest != null && filterRequest.getModule() != null) {
           // String selectQuery = componentHelper.getSelectQueryByBMCompCode(filterRequest.getModule());
            BMCompDetail bmCompDetail = componentHelper.getBMCompDetail(filterRequest.getModule());
            if (bmCompDetail != null && !ObjectUtils.isEmpty(bmCompDetail.getSelectQuery())) {
                FilterResponse filterResponse = jdbcTemplateService.getQueryResult(filterRequest.getPageNo()
                        , filterRequest.getPageSize(), bmCompDetail.getSelectQuery());
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

    public List<MenuResponse> getModuleMenu() throws Exception {
        try {
            return componentHelper.getModuleMenu();
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
}

package com.ims.inventory.helpers;

import com.ims.inventory.domen.entity.BMCompDetail;
import com.ims.inventory.domen.entity.BMCompElements;
import com.ims.inventory.domen.entity.BMComponent;
import com.ims.inventory.domen.response.FilterResponse;
import com.ims.inventory.domen.response.MenuResponse;
import com.ims.inventory.repository.BMCompDetailRepository;
import com.ims.inventory.repository.BMCompElementRepository;
import com.ims.inventory.repository.BMComponentRepository;
import com.ims.inventory.service.JdbcTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component()
public class ComponentHelper {

    private BMComponentRepository bmComponentRepository;
    private BMCompDetailRepository bmCompDetailRepository;
    private BMCompElementRepository bmCompElementRepository;
    private JdbcTemplateService jdbcTemplateSerivce;

    @Autowired
    public ComponentHelper(BMComponentRepository bmComponentRepository,
                                BMCompDetailRepository bmCompDetailRepository,
                                BMCompElementRepository bmCompElementRepository,
                                JdbcTemplateService jdbcTemplateSerivce){
        this.bmComponentRepository = bmComponentRepository;
        this.bmCompDetailRepository = bmCompDetailRepository;
        this.bmCompElementRepository = bmCompElementRepository;
        this.jdbcTemplateSerivce = jdbcTemplateSerivce;
    }

    public List<BMComponent> getAllBMComponent() {
        return bmComponentRepository.findAll();
    }

    public BMComponent getBMComponent(String compCode) throws Exception{
        BMComponent bmComponent = null;
        if(compCode != null && !compCode.isEmpty()){
            bmComponent = bmComponentRepository.findByCode(compCode);
        }else {
            throw new Exception("Input data is null.");
        }
        return bmComponent;
    }

    public BMCompDetail getBMCompDetail(String compCode) throws Exception{
        BMCompDetail bmCompDetail = null;
        if(compCode != null && !compCode.isEmpty()){
            bmCompDetail = bmCompDetailRepository.findByCompCode(compCode);
        }else {
            throw new Exception("Input data is null.");
        }
        return bmCompDetail;
    }

    public String getSelectQueryByBMCompCode(String compCode) throws Exception{
        if(compCode != null && !compCode.isEmpty()){
            String selectQueryStr = bmCompDetailRepository.findSelectQueryByCompCode(compCode);
            if(selectQueryStr != null && !selectQueryStr.isEmpty()){
                return selectQueryStr;
            }else {
                throw new Exception("Select query string not found or null.");
            }
        }else {
            throw new Exception("Input data is null.");
        }
    }

    public String getInsertQueryByBMCompCode(String compCode) throws Exception{
        if(compCode != null && !compCode.isEmpty()){
            String insertQueryStr = bmCompDetailRepository.findInsertQueryByCompCode(compCode);
            if(insertQueryStr != null && !insertQueryStr.isEmpty()){
                return insertQueryStr;
            }else {
                throw new Exception("Insert query string not found or null");
            }
        }else {
            throw new Exception("Input data is null.");
        }
    }

    public String getTableDesignByBMCompCode(String compCode) throws Exception {
        if (compCode != null && !compCode.isEmpty()) {
            BMCompDetail bmCompDetail = bmCompDetailRepository.findByCompCode(compCode);
            if (bmCompDetail != null && ObjectUtils.isNotEmpty(bmCompDetail.getDesign())) {
                return bmCompDetail.getDesign();
            } else {
                throw new Exception("Component detail not found or null");
            }
        } else {
            throw new Exception("Input data is null.");
        }
    }

    public String getDesignByBMCompCode(String compCode, String recordCode) throws Exception {
        if (compCode != null && !compCode.isEmpty()) {
            BMCompDetail bmCompDetail = bmCompDetailRepository.findByCompCode(compCode);
            if (bmCompDetail != null) {
                /*if (null != bmCompDetail.getDesign() && !bmCompDetail.getDesign().isEmpty()) {
                    return  bmCompDetail.getDesign().trim();
                } else*/
                if (null != bmCompDetail.getBmCompElementsList()
                        && !bmCompDetail.getBmCompElementsList().isEmpty()) {
                    Map<String, Object> recordData = null;
                    if (ObjectUtils.isNotEmpty(recordCode)) {
                        recordData = loadRecordByBMCompDetailAndRecordCode(
                                bmCompDetail, recordCode);
                    }
                    JSONArray elementArray = buildFormDesing(bmCompDetail.getBmCompElementsList(), recordData);
                    JSONObject result = new JSONObject();
                    result.put("data", elementArray);
                    result.put("label", bmCompDetail.getBmComponent().getLabel());
                    return result.toString();
                } else {
                    throw new Exception("Component column detail not found or null");
                }
            } else {
                throw new Exception("Design string not found or null");
            }
        } else {
            throw new Exception("Input data is null.");
        }
    }

    private JSONArray buildFormDesing(List<BMCompElements> bmCompElementsList, Map<String, Object> recordData){
        JSONArray elementArray = new JSONArray();
        JSONObject obj = null;
        JSONArray subElements = new JSONArray();
        for (BMCompElements ele : bmCompElementsList) {
            if (null != ele) {
                obj = new JSONObject();
                obj.put("id_editable", ele.getIsEditable());
                obj.put("is_form", ele.getIsForm());
                obj.put("id", ele.getCode());
                obj.put("label", ele.getColName());
                obj.put("type", ele.getColType());
             //   obj.put("positions", new JSONObject(ele.getPositions()));
             //   obj.put("design", new JSONObject(ele.getDesign()));
                obj.put("value", recordData != null ? recordData.getOrDefault(ele.getColLabel(), "") : "");
                subElements.put(obj);
                if (subElements.length() == 2) {
                    elementArray.put(subElements);
                    subElements = new JSONArray();
                }
            }
        }
        return elementArray;
    }

    public void updateDesignByBMCompCode(String compCode, String designStr) throws Exception {
        if (compCode != null && !compCode.isEmpty() && null != designStr) {
            BMCompDetail bmCompDetail = bmCompDetailRepository.findByCompCode(compCode);
            JSONArray desingJsonArray = new JSONArray(designStr);
            if (ObjectUtils.isNotEmpty(bmCompDetail) && ObjectUtils.isNotEmpty(desingJsonArray)) {
                bmCompDetail = updateDesign(bmCompDetail, desingJsonArray);
                bmCompDetail.setDesign(designStr);
                bmCompDetailRepository.save(bmCompDetail);
            } else {
                throw new Exception("Design string not found or null");
            }
        } else {
            throw new Exception("Input data is null.");
        }
    }

    private Map<String, JSONObject> makeDesignMap(JSONArray desingJsonArray) {
        Map<String, JSONObject> mapObj = new HashMap<>();
        for (int i = 0; i < desingJsonArray.length(); i++) {
            JSONObject obj = desingJsonArray.getJSONObject(i);
            mapObj.put(obj.optString("id", ""), obj);
        }
        return mapObj;
    }

    private BMCompDetail updateDesign(BMCompDetail bmCompDetail, JSONArray desingJsonArray) {
        Map<String, JSONObject> mapObj = makeDesignMap(desingJsonArray);
        if (ObjectUtils.isNotEmpty(bmCompDetail.getBmCompElementsList()) && null != mapObj) {
            List<BMCompElements> eleList = new ArrayList<>();
            for (BMCompElements ele : bmCompDetail.getBmCompElementsList()) {
                JSONObject obj = mapObj.get(ele.getCode());
                if (ObjectUtils.isNotEmpty(obj)) {
                    ele.setDesign(obj.optString("design", "{}"));
                    ele.setPositions(obj.optString("positions", "{}"));
                }
                eleList.add(ele);
            }
            bmCompDetail.setBmCompElementsList(eleList);
        }
        return bmCompDetail;
    }

    public FilterResponse getDropDownOptions(String compCode, String colCode) throws Exception {
        if (compCode != null && !compCode.isEmpty() && null != colCode) {
            try {
                BMCompElements bMCompElements = bmCompElementRepository.findByCompCodeAndCode(colCode, compCode);
                if (!ObjectUtils.isEmpty(bMCompElements)) {
                    String selectQuery = bMCompElements.getDropDownQuery();
                    if (ObjectUtils.isNotEmpty(selectQuery)) {
                        Integer pageNo = 1;
                        Integer pageSize = 50;
                        return jdbcTemplateSerivce.getQueryResult( pageNo,
                                pageSize, selectQuery);
                    } else {
                        throw new Exception("Select Query Data Not Found");
                    }
                } else {
                    throw new Exception("selectQuery will be null or empty.");
                }
            } catch (Exception e) {
                log.error("Exception :: getDropDownOptions : {} ", e.getMessage(), e);
                throw e;
            }
        } else {
            throw new Exception("Inpute will be null or empty.");
        }
    }

    public Map<String, Object> loadRecordByBMCompCodeAndRecordCode(String compCode
            , String recordCode) throws Exception {
        if (ObjectUtils.isNotEmpty(compCode) && ObjectUtils.isNotEmpty(recordCode)) {
            BMCompDetail bmCompDetail = bmCompDetailRepository.findByCompCode(compCode);
            return loadRecordByBMCompDetailAndRecordCode(bmCompDetail, recordCode);
        } else {
            throw new Exception("Input data is null.");
        }
    }

    private Map<String, Object> loadRecordByBMCompDetailAndRecordCode(BMCompDetail bmCompDetail
            , String recordCode) throws Exception {
        if (!ObjectUtils.isEmpty(bmCompDetail) && ObjectUtils.isNotEmpty(bmCompDetail.getTableName())) {
            Boolean flag = true;
            List<BMCompElements> bmCompElementsList = bmCompElementRepository
                    .findByBmComIdAndIsForm(bmCompDetail.getId(), flag);
            if (ObjectUtils.isNotEmpty(bmCompElementsList)) {
                String queryCol = buildLoanFormQuery(bmCompElementsList);
                List<Map<String, Object>> list = jdbcTemplateSerivce.executeSelectWithWhereQuery(
                        bmCompDetail.getTableName(), queryCol, recordCode);
                if (ObjectUtils.isNotEmpty(list)) {
                    return list.get(0);
                } else {
                    throw new Exception("data not found");
                }
            } else {
                throw new Exception("Element not found or null");
            }
        } else {
            throw new Exception("BMCompDetail not found or null");
        }
    }

    private String buildLoanFormQuery(List<BMCompElements> bmCompElementsList) {
        StringBuffer sb = new StringBuffer();
        for (BMCompElements ele : bmCompElementsList) {
            if (!ObjectUtils.isEmpty(ele)) {
                if (sb.isEmpty()) {
                    sb.append(ele.getColLabel());
                } else {
                    sb.append(", ").append(ele.getColLabel());
                }
            }
        }
        return sb.toString();
    }

    public List<MenuResponse> getModuleMenu() {
        try {
            List<BMComponent> bmComponentList = bmComponentRepository.findByParentIsNullOrderByOrder();
            List<MenuResponse> menuRespList = new ArrayList<>();
            if (null != bmComponentList && !bmComponentList.isEmpty()) {
                for (BMComponent bmComp : bmComponentList) {
                    if(null != bmComp){
                        menuRespList.add(createMenu(bmComp));
                    }
                }
            }
            return menuRespList;
        } catch (Exception e) {
            log.error("Exception occurs in getting menu.");
            throw e;
        }
    }

    private MenuResponse createMenu(BMComponent bmComp) {
        MenuResponse menuResp = new MenuResponse();
        if (ObjectUtils.isNotEmpty(bmComp.getChildren())) {
            List<MenuResponse> menuItems = new ArrayList<>();
            for (BMComponent child : bmComp.getChildren()) {
                menuItems.add(createMenu(child));
            }
            menuResp.setLinkCode(bmComp.getCode());
            menuResp.setLinkName(bmComp.getDescription());
            menuResp.setIcon(bmComp.getIcon());
            menuResp.setLink(bmComp.getLink());
            menuResp.setSubMenu(menuItems);
        } else {
            menuResp.setLinkCode(bmComp.getCode());
            menuResp.setLinkName(bmComp.getDescription());
            menuResp.setIcon(bmComp.getIcon());
            menuResp.setLink(bmComp.getLink());
            menuResp.setSubMenu(new ArrayList<>());
        }
        return menuResp;
    }

}

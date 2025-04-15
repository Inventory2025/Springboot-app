package com.ims.inventory.domen.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterResponse {

    private String label;
    private String subModuleLink;
    private Boolean isAdd;
    private Page page;
    private List<Map<String, Object>> resultList;

}

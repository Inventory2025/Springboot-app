package com.ims.inventory.domen.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    private Integer pageNo;
    private Integer pageSize;
    private SortBy sortBy;
    private String Module;
    private Object data;
}

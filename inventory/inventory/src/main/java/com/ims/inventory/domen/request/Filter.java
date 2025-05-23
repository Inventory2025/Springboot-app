package com.ims.inventory.domen.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter {

    private String column;
    private String operator;
    private String condition;
    private String dataType;

    private List<FilterValue> values;

}

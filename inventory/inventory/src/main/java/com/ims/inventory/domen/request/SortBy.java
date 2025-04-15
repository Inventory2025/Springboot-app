package com.ims.inventory.domen.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortBy {
    private String sortBy;
    private String direction;
}

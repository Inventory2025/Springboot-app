package com.ims.inventory.domen.dto;

import lombok.Data;

@Data
public class UnitDropDownDto {
    private String id;
    private String name;
    private String code;

    public UnitDropDownDto(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}

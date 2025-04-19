package com.ims.inventory.domen.dto;

import lombok.Data;

@Data
public class RoleDto {
    private String id;
    private String name;
    private String code;

    public RoleDto(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}

package com.ims.inventory.domen.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequest {
    private String module;
    private String data;
    private String recordCode;
}

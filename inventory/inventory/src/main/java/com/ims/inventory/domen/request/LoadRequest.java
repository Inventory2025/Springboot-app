package com.ims.inventory.domen.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequest {
    private String module;
    private String recordCode;
}

package com.ims.inventory.domen.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Responce {
    private Long code;
    private String message;
    private Object result;
}

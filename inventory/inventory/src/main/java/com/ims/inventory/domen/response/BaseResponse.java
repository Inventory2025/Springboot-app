package com.ims.inventory.domen.response;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseResponse {

    private String status;
    private String message;

}

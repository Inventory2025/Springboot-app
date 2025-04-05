package com.ims.inventory.domen.response;

import lombok.Data;

@Data
public class UserDetailResponse {

    private Long id;
    private String username;
    private String emailId;
    private String password;

}

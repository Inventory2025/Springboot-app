package com.ims.inventory.domen.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class AuthenticationResponse {

    private String username;
    private String tkn;
}

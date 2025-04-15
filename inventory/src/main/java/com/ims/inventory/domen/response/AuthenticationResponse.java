package com.ims.inventory.domen.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class AuthenticationResponse {

    private String username;
    private String tkn;
}

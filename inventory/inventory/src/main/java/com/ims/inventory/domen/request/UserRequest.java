package com.ims.inventory.domen.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {

    @NotBlank(message = "Username should not null or empty.")
    private String userName;

    @NotBlank(message = "Phone number should not null or empty.")
    private String phoneNumber;

    @NotBlank(message = "Email should not null or empty.")
    private String email;

    @NotBlank(message = "Password should not null or empty.")
    private String password;

    private String firstName;
    private String middleName;
    private String lastName;
    private String description;
}

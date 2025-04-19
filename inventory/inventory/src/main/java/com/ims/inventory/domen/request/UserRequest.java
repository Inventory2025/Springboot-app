package com.ims.inventory.domen.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("username")
    private String userName;

    @NotBlank(message = "Phone number should not null or empty.")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Email should not null or empty.")
    @JsonProperty("email_id")
    private String email;

    @NotBlank(message = "Password should not null or empty.")
    @JsonProperty("password")
    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("middel_name")
    private String middleName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("confirm_password")
    private String confirmPassword;

    @JsonProperty("branch_id")
    private String branchId;

    @JsonProperty("role_id")
    private String roleId;
}

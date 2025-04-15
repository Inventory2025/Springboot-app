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
public class CustomerRequest {
    @NotBlank(message = "Customer name should not null or empty.")
    private String customer_Name;
    private String email;
    private String phone_Number;
}

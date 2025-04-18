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
    private String customerName;
    @NotBlank(message = "Customer Email should not null or empty.")
    private String email;
    @NotBlank(message = "Customer Phone number should not null or empty.")
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String state;
    private String country;
    private String pinCode;

}

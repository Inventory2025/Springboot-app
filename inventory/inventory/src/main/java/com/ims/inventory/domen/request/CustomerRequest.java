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
public class CustomerRequest {

    @NotBlank(message = "customerName should not null or empty.")
    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("city_id")
    private String cityId;

    @JsonProperty("state_id")
    private String stateId;

    @JsonProperty("country_id")
    private String countryId;

}

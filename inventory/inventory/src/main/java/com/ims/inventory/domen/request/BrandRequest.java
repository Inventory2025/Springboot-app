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
public class BrandRequest {
    @NotBlank(message = "Brand name should not null or empty.")
    @JsonProperty("name")
    private String name;

    @JsonProperty("cateory_id")
    private String categoryId;

    @JsonProperty("brand_id")
    private String brandId;

    @JsonProperty("price")
    private String price;

    @JsonProperty("unit_id")
    private String unitId;

    @JsonProperty("quantity")
    private String quantity;
}

package com.ims.inventory.domen.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveRequest {

    @NotBlank(message = "id should not null or empty.")
    private String id;

}

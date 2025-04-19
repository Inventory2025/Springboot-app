package com.ims.inventory.domen.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequest {

    private String module;

    @NotNull(message = "Record code is required.")
    private String recordCode;
}

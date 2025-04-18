package com.ims.inventory.exception;

import lombok.Data;

@Data
public class ImportError {

    private int rowNumber;
    private String errorMessage;

    public ImportError(int rowNumber, String errorMessage) {
        this.rowNumber = rowNumber;
        this.errorMessage = errorMessage;
    }

}

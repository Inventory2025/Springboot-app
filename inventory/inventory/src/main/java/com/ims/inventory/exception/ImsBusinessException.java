package com.ims.inventory.exception;

import lombok.Getter;

@Getter
public class ImsBusinessException extends Exception{

    private final String errorCode;
    private final String errorMessage;

    public ImsBusinessException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}

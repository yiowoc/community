package com.yiowoc.community.exception;

import lombok.Getter;

@Getter
public class CustomizeException extends RuntimeException{
    private String message;
    private Integer code;

    public CustomizeException(CustomizeErrorCode errorCode) {
        message = errorCode.getMessage();
        code = errorCode.getCode();
    }
}

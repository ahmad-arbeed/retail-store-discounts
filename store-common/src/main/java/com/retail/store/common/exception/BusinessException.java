package com.retail.store.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private HttpStatus httpStatus;
    private String code;
    private Object[] args;

    public BusinessException(String code, String message, HttpStatus httpStatus, Throwable cause, Object... args) {
        super(message, cause);
        this.code = code;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    public BusinessException(String code, String message, HttpStatus httpStatus, Object... args) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    public BusinessException(ApiErrors apiErrors, HttpStatus httpStatus, Object... args) {
        super(apiErrors.getMessage());
        this.code = apiErrors.getCode();
        this.httpStatus = httpStatus;
        this.args = args;
    }
}
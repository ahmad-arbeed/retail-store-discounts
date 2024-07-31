package com.retail.store.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrors {

    INTERNAL_ERROR("RS-001", "internal server error"),
    NOT_FOUND("RS-002", "no record found for id: {0}"),
    VALIDATION_ERROR("RS-003", "validation errors");

    private final String code;
    private final String message;
}
package com.retail.store.common.exception;

import com.retail.store.common.api.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import java.text.MessageFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(e.getHttpStatus())
            .body(ApiResponse.builder()
                .errorCode(e.getCode())
                .errorMessage(MessageFormat.format(e.getMessage(), e.getArgs()))
                .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        final var errors = e.getConstraintViolations()
            .stream()
            .map(cv -> new ApiResponse.ValidationError(cv.getPropertyPath().toString(), cv.getMessage()))
            .toList();
        return ResponseEntity.badRequest()
            .body(ApiResponse.builder()
                .errorMessage(ApiErrors.VALIDATION_ERROR.getMessage())
                .errorCode(ApiErrors.VALIDATION_ERROR.getCode())
                .validationErrors(errors)
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final var errors = e.getFieldErrors()
            .stream()
            .map(fe -> new ApiResponse.ValidationError(fe.getField(), fe.getDefaultMessage()))
            .toList();
        return ResponseEntity.badRequest()
            .body(ApiResponse.builder()
                .errorMessage(ApiErrors.VALIDATION_ERROR.getMessage())
                .errorCode(ApiErrors.VALIDATION_ERROR.getCode())
                .validationErrors(errors)
                .build());
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ApiResponse<Object>> handleException(InternalException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.builder()
                .errorCode(ApiErrors.INTERNAL_ERROR.getCode())
                .errorMessage(ApiErrors.INTERNAL_ERROR.getMessage())
                .messageDetails(e.getMessage())
                .build());
    }

    @ExceptionHandler(FeignClientException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(FeignClientException e) {
        return e.getResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.builder()
                .errorCode(ApiErrors.INTERNAL_ERROR.getCode())
                .errorMessage(ApiErrors.INTERNAL_ERROR.getMessage())
                .messageDetails(e.getMessage())
                .build());
    }
}

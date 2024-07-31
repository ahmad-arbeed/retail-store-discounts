package com.retail.store.common.exception;

import com.retail.store.common.api.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Getter
public class FeignClientException extends RuntimeException {

    private final ResponseEntity<ApiResponse<Void>> responseEntity;
}
package com.retail.store.discount.controller;

import com.retail.store.common.api.ApiResponse;
import com.retail.store.discount.dto.BillRequest;
import com.retail.store.discount.dto.BillResponse;
import com.retail.store.discount.service.DiscountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiscountsController {

    private final DiscountsService service;

    @Operation(summary = "Calculate net payable amount")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "calculated successfully", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = BillResponse.class))})})
    @PostMapping("/net-payable-amount")
    public ResponseEntity<ApiResponse<BillResponse>> netPayableAmount(@RequestBody @Valid BillRequest billRequest) {
        return ResponseEntity.ok(ApiResponse.<BillResponse>builder()
            .response(service.netPayableAmount(billRequest))
            .build());
    }
}

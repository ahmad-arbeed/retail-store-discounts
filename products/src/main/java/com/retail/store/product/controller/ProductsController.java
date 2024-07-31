package com.retail.store.product.controller;

import com.retail.store.common.api.ApiResponse;
import com.retail.store.product.dto.ProductDto;
import com.retail.store.product.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService service;

    @Operation(summary = "Create product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "product created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))})})
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody @Valid ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.<ProductDto>builder()
                .response(service.createProduct(productDto))
                .build());
    }

    @Operation(summary = "Update product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "product updated", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))}),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "not product found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))})})
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@PathVariable("id") long id, @RequestBody @Valid ProductDto productDto) {
        return ResponseEntity.ok(ApiResponse.<ProductDto>builder()
            .response(service.updateProduct(id, productDto))
            .build());
    }

    @Operation(summary = "Delete product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "product deleted"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "not product found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))})})
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable("id") long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get product by id")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "product found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))}),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "not product found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))})})
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable("id") long id) {
        return ResponseEntity.ok(ApiResponse.<ProductDto>builder()
            .response(service.getProduct(id))
            .build());
    }

    @Operation(summary = "Get all products")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "products returned if any", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))})})
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getAllProducts(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = page < 0 || size < 1 ? Pageable.unpaged() : PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.<Page<ProductDto>>builder()
            .response(service.getAllProducts(pageable))
            .build());
    }

    @Operation(summary = "Get all products by ids")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "products returned", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))})})
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProductsByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(ApiResponse.<List<ProductDto>>builder()
            .response(service.getAllProductsByIds(ids))
            .build());
    }
}

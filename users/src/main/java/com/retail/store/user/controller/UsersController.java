package com.retail.store.user.controller;

import com.retail.store.common.api.ApiResponse;
import com.retail.store.user.dto.UserDto;
import com.retail.store.user.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
public class UsersController {

    private final UsersService service;

    @Operation(summary = "Create user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "user created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))})})
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.<UserDto>builder()
                .response(service.createUser(userDto))
                .build());
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "user updated", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))}),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "not user found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))})})
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable("id") long id, @RequestBody @Valid
    UserDto userDto) {
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
            .response(service.updateUser(id, userDto))
            .build());
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "user deleted"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "not user found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))})})
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "user found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))}),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "not user found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))})})
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
            .response(service.getUser(id))
            .build());
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "users returned if any", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))})})
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDto>>> getAllUsers(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = page < 0 || size < 1 ? Pageable.unpaged() : PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.<Page<UserDto>>builder()
            .response(service.getAllUsers(pageable))
            .build());
    }
}

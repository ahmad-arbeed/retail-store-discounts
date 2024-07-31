package com.retail.store.user.dto;

import com.retail.store.user.constant.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UserDto {

    private long id;
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    @NotNull
    private UserType userType;
    private Instant createdDate;
    private Instant modifiedDate;
}
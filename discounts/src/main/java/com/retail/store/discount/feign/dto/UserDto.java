package com.retail.store.discount.feign.dto;

import com.retail.store.discount.constant.UserType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private long id;
    private String name;
    private UserType userType;
    private Instant createdDate;
    private Instant modifiedDate;
}

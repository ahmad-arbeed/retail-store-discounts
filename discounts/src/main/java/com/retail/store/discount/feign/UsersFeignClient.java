package com.retail.store.discount.feign;

import com.retail.store.common.api.ApiResponse;
import com.retail.store.discount.feign.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service-client", url = "${feign.users.service.url}", configuration = CustomFeignClientConfig.class)
public interface UsersFeignClient {

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable("id") long id);
}

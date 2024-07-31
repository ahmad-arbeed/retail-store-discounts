package com.retail.store.discount.feign;

import com.retail.store.common.api.ApiResponse;
import com.retail.store.discount.feign.dto.ProductDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "products-service-client", url = "${feign.products.service.url}", configuration = CustomFeignClientConfig.class)
public interface ProductsFeignClient {

    @GetMapping("/all")
    ResponseEntity<ApiResponse<List<ProductDto>>> getAllProductsByIds(@RequestParam("ids") List<Long> ids);
}

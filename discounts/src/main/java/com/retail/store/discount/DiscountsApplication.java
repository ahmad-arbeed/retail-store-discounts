package com.retail.store.discount;

import com.retail.store.discount.feign.ProductsFeignClient;
import com.retail.store.discount.feign.UsersFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = {ProductsFeignClient.class, UsersFeignClient.class})
@SpringBootApplication(scanBasePackages = {"com.retail.store.discount", "com.retail.store.common"})
public class DiscountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscountsApplication.class, args);
    }
}
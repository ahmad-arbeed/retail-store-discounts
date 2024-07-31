package com.retail.store.discount.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.store.common.api.ApiResponse;
import com.retail.store.common.exception.FeignClientException;
import com.retail.store.common.exception.InternalException;
import feign.Logger;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

@Slf4j
public class CustomFeignClientConfig {

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder getErrorDecoder(ObjectMapper objectMapper) {
        return (methodKey, response) -> {

            final var status = response.status();
            log.error("Feign Client Response Error. Method Key: {}, Status: {}", methodKey, status);
            if (status >= 400 && status <= 499 && status != 401) {
                try {
                    final var body = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
                    final var apiResponse = objectMapper.readValue(body, new TypeReference<ApiResponse<Void>>() {
                    });
                    return new FeignClientException(ResponseEntity.status(status).body(apiResponse));
                } catch (IOException e) {
                    log.error("Feign Client Response: {}", response);
                    return new InternalException("Error while trying to read the response body");
                }
            } else {
                log.error("Feign Client Response: {}", response);
                return new InternalException("Not supported Error was returned with error code:" + status);
            }
        };
    }
}

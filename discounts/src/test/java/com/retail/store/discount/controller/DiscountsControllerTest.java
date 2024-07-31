package com.retail.store.discount.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.retail.store.common.api.ApiResponse;
import com.retail.store.common.service.SequenceGeneratorServiceImpl;
import com.retail.store.discount.constant.Category;
import com.retail.store.discount.constant.UserType;
import com.retail.store.discount.dto.BillRequest;
import com.retail.store.discount.dto.ItemDto;
import com.retail.store.discount.feign.ProductsFeignClient;
import com.retail.store.discount.feign.UsersFeignClient;
import com.retail.store.discount.feign.dto.ProductDto;
import com.retail.store.discount.feign.dto.UserDto;
import com.retail.store.discount.model.Bill;
import com.retail.store.discount.model.Item;
import com.retail.store.discount.repository.BillRepository;
import com.retail.store.discount.service.DiscountsService;
import com.retail.store.discount.service.DiscountsServiceImpl;
import com.retail.store.discount.service.discount.factory.PercentageDiscountStrategyFactory;
import com.retail.store.discount.service.discount.strategy.AffiliatePercentageDiscountStrategy;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ComponentScan("com.retail.store.discount.mapper")
@WebMvcTest(DiscountsController.class)
class DiscountsControllerTest {

    @Autowired
    MockMvc mvc;

    @SpyBean
    DiscountsServiceImpl discountsService;
    @SpyBean
    AffiliatePercentageDiscountStrategy strategy;
    @MockBean
    PercentageDiscountStrategyFactory factory;
    @MockBean
    UsersFeignClient usersClient;
    @MockBean
    ProductsFeignClient productsClient;
    @MockBean
    BillRepository repository;
    @MockBean
    SequenceGeneratorServiceImpl sequenceGenerator;

    @Test
    void testNetPayableAmount_successResponse() throws Exception {

        final var user = new UserDto(1L, "Ahmad", UserType.AFFILIATE, Instant.now(), null);
        when(usersClient.getUserById(anyLong()))
            .thenReturn(ResponseEntity.ok(ApiResponse.<UserDto>builder().response(user).build()));

        final var product1 = new ProductDto(1L, "something1", Category.ELECTRONIC, BigDecimal.TEN);
        final var product2 = new ProductDto(2L, "something2", Category.GROCERIES, BigDecimal.ONE);
        when(productsClient.getAllProductsByIds(any()))
            .thenReturn(ResponseEntity.ok(ApiResponse.<List<ProductDto>>builder().response(List.of(product1, product2)).build()));

        when(factory.getPercentageDiscountStrategy(any(), any())).thenReturn(strategy);
        when(sequenceGenerator.generateSequence(any(), any())).thenReturn(1L);

        final var bill = Bill.builder()
            .id(1L)
            .userId(1L)
            .totalAmount(new BigDecimal("25"))
            .discountAmount(new BigDecimal("2"))
            .netPayableAmount(new BigDecimal("23"))
            .items(List.of(new Item()))
            .createdDate(Instant.now())
            .build();
        when(repository.save(any())).thenReturn(bill);

        final var request = """
            {
                "userId": 1,
                "items": [
                    {
                        "itemId": 1,
                        "quantity": 2
                    },
                    {
                        "itemId": 2,
                        "quantity": 5
                    }
                ]
            }
            """;

        final var response = """
            {
                "date": "2024-07-31T14:18:18.655252Z",
                "response": {
                    "id": 1,
                    "userId": 1,
                    "items": [
                        {
                            "itemId": 1,
                            "quantity": 2,
                            "name": "something1",
                            "price": 10,
                            "totalPrice": 20,
                            "category": "ELECTRONIC"
                        },
                        {
                            "itemId": 2,
                            "quantity": 5,
                            "name": "something2",
                            "price": 1,
                            "totalPrice": 5,
                            "category": "GROCERIES"
                        }
                    ],
                    "totalAmount": 25,
                    "discountAmount": 2.0,
                    "netPayableAmount": 23.0
                }
            }
            """;

        final var result = mvc.perform(MockMvcRequestBuilders.post("/net-payable-amount")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(request))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        JsonAssertions.assertThatJson(result.getResponse().getContentAsString())
            .whenIgnoringPaths("date")
            .isEqualTo(response);
    }
}

package com.retail.store.discount.service;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.retail.store.common.api.ApiResponse;
import com.retail.store.common.exception.FeignClientException;
import com.retail.store.common.service.SequenceGeneratorService;
import com.retail.store.discount.constant.Category;
import com.retail.store.discount.constant.UserType;
import com.retail.store.discount.dto.BillRequest;
import com.retail.store.discount.dto.ItemDto;
import com.retail.store.discount.feign.ProductsFeignClient;
import com.retail.store.discount.feign.UsersFeignClient;
import com.retail.store.discount.feign.dto.ProductDto;
import com.retail.store.discount.feign.dto.UserDto;
import com.retail.store.discount.mapper.BillMapper;
import com.retail.store.discount.model.Bill;
import com.retail.store.discount.model.Item;
import com.retail.store.discount.repository.BillRepository;
import com.retail.store.discount.service.discount.factory.PercentageDiscountStrategyFactory;
import com.retail.store.discount.service.discount.strategy.AffiliatePercentageDiscountStrategy;
import com.retail.store.discount.service.discount.strategy.CustomerPercentageDiscountStrategy;
import com.retail.store.discount.service.discount.strategy.EmployeePercentageDiscountStrategy;
import com.retail.store.discount.service.discount.strategy.NoPercentageDiscountStrategy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DiscountsServiceTest {

    ProductsFeignClient productsClient = Mockito.mock(ProductsFeignClient.class);
    UsersFeignClient usersClient = Mockito.mock(UsersFeignClient.class);
    BillRepository repository = Mockito.mock(BillRepository.class);
    SequenceGeneratorService sequenceGenerator = Mockito.mock(SequenceGeneratorService.class);
    BillMapper mapper = Mappers.getMapper(BillMapper.class);

    PercentageDiscountStrategyFactory factory = Mockito.spy(
        new PercentageDiscountStrategyFactory(
            new EmployeePercentageDiscountStrategy(),
            new AffiliatePercentageDiscountStrategy(),
            new CustomerPercentageDiscountStrategy(),
            new NoPercentageDiscountStrategy()));

    DiscountsService discountsService = Mockito.spy(
        new DiscountsServiceImpl(productsClient, usersClient, repository, factory, sequenceGenerator, mapper));

    @BeforeEach
    public void init() {
        final var product1 = new ProductDto(1L, "something", Category.ELECTRONIC, BigDecimal.TEN);
        final var product2 = new ProductDto(2L, "something", Category.GROCERIES, BigDecimal.ONE);
        when(productsClient.getAllProductsByIds(any()))
            .thenReturn(ResponseEntity.ok(ApiResponse.<List<ProductDto>>builder().response(List.of(product1, product2)).build()));
    }

    @Test
    void testNetPayableAmount() {

        final var request = BillRequest.builder()
            .items(List.of(ItemDto.builder().itemId(1L).quantity(2).build(),
                ItemDto.builder().itemId(2L).quantity(5).build()))
            .userId(1L)
            .build();

        final var user = new UserDto(1L, "Ahmad", UserType.EMPLOYEE, Instant.now(), null);
        when(usersClient.getUserById(anyLong()))
            .thenReturn(ResponseEntity.ok(ApiResponse.<UserDto>builder().response(user).build()));

        final var bill = Bill.builder()
            .id(1L)
            .userId(1L)
            .totalAmount(new BigDecimal("25"))
            .discountAmount(new BigDecimal("6"))
            .netPayableAmount(new BigDecimal("19"))
            .items(List.of(new Item()))
            .createdDate(Instant.now())
            .build();
        when(repository.save(any())).thenReturn(bill);

        final var response = discountsService.netPayableAmount(request);
        Assertions.assertEquals(new BigDecimal("19.0"), response.getNetPayableAmount());
        Assertions.assertEquals(new BigDecimal("25"), response.getTotalAmount());
        Assertions.assertEquals(new BigDecimal("6.0"), response.getDiscountAmount());
    }

    @Test
    void testNetPayableAmount_customerUserTypeOver2Years() {

        final var request = BillRequest.builder()
            .items(List.of(ItemDto.builder().itemId(1L).quantity(2).build(),
                ItemDto.builder().itemId(2L).quantity(5).build()))
            .userId(1L)
            .build();

        final var user = new UserDto(1L, "Ahmad", UserType.CUSTOMER, Instant.now().minus(365 * 3, ChronoUnit.DAYS), null);
        when(usersClient.getUserById(anyLong()))
            .thenReturn(ResponseEntity.ok(ApiResponse.<UserDto>builder().response(user).build()));

        final var bill = Bill.builder()
            .id(1L)
            .userId(1L)
            .totalAmount(new BigDecimal("25"))
            .discountAmount(new BigDecimal("1"))
            .netPayableAmount(new BigDecimal("24"))
            .items(List.of(new Item()))
            .createdDate(Instant.now())
            .build();
        when(repository.save(any())).thenReturn(bill);

        final var response = discountsService.netPayableAmount(request);
        Assertions.assertEquals(new BigDecimal("24.00"), response.getNetPayableAmount());
        Assertions.assertEquals(new BigDecimal("25"), response.getTotalAmount());
        Assertions.assertEquals(new BigDecimal("1.00"), response.getDiscountAmount());
    }

    @Test
    void testNetPayableAmount_customerUserType() {

        final var request = BillRequest.builder()
            .items(List.of(ItemDto.builder().itemId(1L).quantity(2).build(),
                ItemDto.builder().itemId(2L).quantity(5).build()))
            .userId(1L)
            .build();

        final var user = new UserDto(1L, "Ahmad", UserType.CUSTOMER, Instant.now(), null);
        when(usersClient.getUserById(anyLong()))
            .thenReturn(ResponseEntity.ok(ApiResponse.<UserDto>builder().response(user).build()));

        final var bill = Bill.builder()
            .id(1L)
            .userId(1L)
            .netPayableAmount(new BigDecimal("25"))
            .totalAmount(new BigDecimal("25"))
            .discountAmount(new BigDecimal("0"))
            .items(List.of(new Item()))
            .createdDate(Instant.now())
            .build();
        when(repository.save(any())).thenReturn(bill);

        final var response = discountsService.netPayableAmount(request);
        Assertions.assertEquals(new BigDecimal("25"), response.getNetPayableAmount());
        Assertions.assertEquals(new BigDecimal("25"), response.getTotalAmount());
        Assertions.assertEquals(new BigDecimal("0"), response.getDiscountAmount());
    }

    @Test
    void testNetPayableAmount_userNotFound() {

        final var request = BillRequest.builder()
            .items(List.of(ItemDto.builder().itemId(1L).quantity(2).build(),
                ItemDto.builder().itemId(2L).quantity(5).build()))
            .userId(1L)
            .build();

        when(usersClient.getUserById(anyLong())).thenThrow(FeignClientException.class);
        Assertions.assertThrows(FeignClientException.class, () -> discountsService.netPayableAmount(request));
    }

    @Test
    void testGetPercentageDiscountStrategyForEmployee() {
        final var userType = UserType.EMPLOYEE;
        final var strategy = factory.getPercentageDiscountStrategy(userType, Instant.now());
        assertInstanceOf(EmployeePercentageDiscountStrategy.class, strategy);
    }

    @Test
    void testGetPercentageDiscountStrategyForAffiliate() {
        final var userType = UserType.AFFILIATE;
        final var strategy = factory.getPercentageDiscountStrategy(userType, Instant.now());
        assertInstanceOf(AffiliatePercentageDiscountStrategy.class, strategy);
    }

    @Test
    void testGetPercentageDiscountStrategyForCustomerOver2Years() {
        final var userType = UserType.CUSTOMER;
        final var createdDate = Instant.now().minus(365 * 3, ChronoUnit.DAYS);
        final var strategy = factory.getPercentageDiscountStrategy(userType, createdDate);
        assertInstanceOf(CustomerPercentageDiscountStrategy.class, strategy);
    }

    @Test
    void testGetPercentageDiscountStrategyForCustomerLessThan2Years() {
        final var userType = UserType.CUSTOMER;
        final var createdDate = Instant.now().minus(1, ChronoUnit.DAYS);
        final var strategy = factory.getPercentageDiscountStrategy(userType, createdDate);
        assertInstanceOf(NoPercentageDiscountStrategy.class, strategy);
    }
}

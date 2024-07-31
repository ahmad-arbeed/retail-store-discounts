package com.retail.store.discount.service;

import com.retail.store.common.api.ApiResponse;
import com.retail.store.common.exception.InternalException;
import com.retail.store.common.service.SequenceGeneratorService;
import com.retail.store.discount.constant.Category;
import com.retail.store.discount.dto.BillRequest;
import com.retail.store.discount.dto.BillResponse;
import com.retail.store.discount.dto.ItemDto;
import com.retail.store.discount.feign.ProductsFeignClient;
import com.retail.store.discount.feign.UsersFeignClient;
import com.retail.store.discount.feign.dto.ProductDto;
import com.retail.store.discount.feign.dto.UserDto;
import com.retail.store.discount.mapper.BillMapper;
import com.retail.store.discount.model.Bill;
import com.retail.store.discount.model.BillSequence;
import com.retail.store.discount.model.Item;
import com.retail.store.discount.repository.BillRepository;
import com.retail.store.discount.service.discount.decorator.BillAmountDiscountDecorator;
import com.retail.store.discount.service.discount.decorator.PercentageDiscountDecorator;
import com.retail.store.discount.service.discount.factory.PercentageDiscountStrategyFactory;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountsServiceImpl implements DiscountsService {

    private final ProductsFeignClient productsClient;
    private final UsersFeignClient usersClient;
    private final BillRepository repository;
    private final PercentageDiscountStrategyFactory factory;
    private final SequenceGeneratorService sequenceGenerator;
    private final BillMapper mapper;

    @Override
    public BillResponse netPayableAmount(BillRequest billRequest) {

        final var user = getUser(billRequest.getUserId());

        final var itemMap = billRequest.getItems()
            .stream()
            .collect(Collectors.toMap(ItemDto::getItemId, ItemDto::getQuantity));
        final var products = getProducts(List.of(itemMap.keySet().toArray(new Long[0])));

        var totalAmount = BigDecimal.ZERO;
        var totalAmountToDiscount = BigDecimal.ZERO;
        final var items = new ArrayList<Item>();
        for (var product : products) {
            final var quantity = itemMap.get(product.getId());
            final var totalPrice = product.getPrice().multiply(new BigDecimal(quantity));

            totalAmount = totalAmount.add(totalPrice);
            if (product.getCategory() != Category.GROCERIES) {
                totalAmountToDiscount = totalAmountToDiscount.add(totalPrice);
            }

            items.add(new Item(product.getId(), product.getName(), quantity, product.getPrice(), totalPrice, product.getCategory()));
        }

        final var discount = getDiscount(user, totalAmountToDiscount);
        final var netPayableAmount = totalAmount.subtract(discount);

        final var bill = Bill.builder()
            .id(sequenceGenerator.generateSequence(Bill.SEQUENCE, BillSequence.class))
            .totalAmount(totalAmount)
            .createdDate(Instant.now())
            .discountAmount(discount)
            .netPayableAmount(netPayableAmount)
            .userId(user.getId())
            .items(items)
            .build();

        repository.save(bill);
        return mapper.map(bill);
    }

    private BigDecimal getDiscount(UserDto user, BigDecimal totalAmountToDiscount) {
        final var strategy = factory.getPercentageDiscountStrategy(user.getUserType(), user.getCreatedDate());
        final var discountDecorator = new BillAmountDiscountDecorator(new PercentageDiscountDecorator(strategy));
        return discountDecorator.applyDiscount(totalAmountToDiscount);
    }

    private UserDto getUser(Long userId) {
        return Optional.ofNullable(usersClient.getUserById(userId))
            .map(ResponseEntity::getBody)
            .map(ApiResponse::getResponse)
            .orElseThrow(() -> new InternalException("something went wrong"));
    }

    private List<ProductDto> getProducts(List<Long> itemIds) {
        return Optional.ofNullable(productsClient.getAllProductsByIds(itemIds))
            .map(ResponseEntity::getBody)
            .map(ApiResponse::getResponse)
            .orElseThrow(() -> new InternalException("something went wrong"));
    }
}

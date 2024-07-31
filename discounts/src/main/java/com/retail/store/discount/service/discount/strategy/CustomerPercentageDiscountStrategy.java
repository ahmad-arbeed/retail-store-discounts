package com.retail.store.discount.service.discount.strategy;

import com.retail.store.discount.service.discount.Discount;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class CustomerPercentageDiscountStrategy implements Discount {

    @Override
    public BigDecimal applyDiscount(final BigDecimal totalAmount) {
        return totalAmount.multiply(new BigDecimal("0.05"));
    }
}
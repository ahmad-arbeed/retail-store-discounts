package com.retail.store.discount.service.discount.strategy;

import com.retail.store.discount.service.discount.Discount;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class NoPercentageDiscountStrategy implements Discount {

    @Override
    public BigDecimal applyDiscount(final BigDecimal totalAmount) {
        return BigDecimal.ZERO;
    }
}
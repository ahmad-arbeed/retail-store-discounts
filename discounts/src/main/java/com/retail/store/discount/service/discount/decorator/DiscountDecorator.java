package com.retail.store.discount.service.discount.decorator;

import com.retail.store.discount.service.discount.Discount;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class DiscountDecorator implements Discount {

    private final Discount discount;

    @Override
    public BigDecimal applyDiscount(final BigDecimal totalAmount) {
        return discount.applyDiscount(totalAmount);
    }
}

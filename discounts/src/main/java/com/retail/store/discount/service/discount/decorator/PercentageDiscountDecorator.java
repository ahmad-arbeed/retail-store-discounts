package com.retail.store.discount.service.discount.decorator;

import com.retail.store.discount.service.discount.Discount;
import java.math.BigDecimal;

public class PercentageDiscountDecorator extends DiscountDecorator {

    public PercentageDiscountDecorator(Discount discount) {
        super(discount);
    }

    @Override
    public BigDecimal applyDiscount(final BigDecimal totalAmount) {
        return super.applyDiscount(totalAmount);
    }
}

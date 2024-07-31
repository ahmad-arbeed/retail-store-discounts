package com.retail.store.discount.service.discount.decorator;

import com.retail.store.discount.service.discount.Discount;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BillAmountDiscountDecorator extends DiscountDecorator {

    public BillAmountDiscountDecorator(Discount discount) {
        super(discount);
    }

    @Override
    public BigDecimal applyDiscount(final BigDecimal totalAmount) {
        var discountAmount = totalAmount.divide(new BigDecimal("100"), RoundingMode.FLOOR).multiply(new BigDecimal("5"));
        return super.applyDiscount(totalAmount).add(discountAmount);
    }
}

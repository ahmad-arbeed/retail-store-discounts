package com.retail.store.discount.service.discount;

import java.math.BigDecimal;

public interface Discount {

    BigDecimal applyDiscount(BigDecimal totalAmount);
}

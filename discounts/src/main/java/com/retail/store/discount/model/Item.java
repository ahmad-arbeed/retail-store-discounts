package com.retail.store.discount.model;

import com.retail.store.discount.constant.Category;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item {

    private long id;
    private String name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Category category;
}
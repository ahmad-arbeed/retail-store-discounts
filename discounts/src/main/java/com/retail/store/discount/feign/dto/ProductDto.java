package com.retail.store.discount.feign.dto;

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
public class ProductDto {

    private long id;
    private String name;
    private Category category;
    private BigDecimal price;
}

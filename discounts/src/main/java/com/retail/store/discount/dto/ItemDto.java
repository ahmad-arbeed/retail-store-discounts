package com.retail.store.discount.dto;

import com.retail.store.discount.constant.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ItemDto {

    @NotNull
    private Long itemId;
    @NotNull
    @Min(1)
    private Integer quantity;
    private String name;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Category category;
}

package com.retail.store.product.dto;

import com.retail.store.product.constant.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class ProductDto {

    private long id;
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    @NotNull
    private Category category;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal price;
    private Instant createdDate;
    private Instant modifiedDate;
}
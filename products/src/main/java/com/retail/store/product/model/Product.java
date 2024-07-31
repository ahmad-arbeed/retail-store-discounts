package com.retail.store.product.model;

import com.retail.store.product.constant.Category;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Document("products")
public class Product {

    @Transient
    public static final String SEQUENCE = "product_sequence";

    @Id
    private long id;
    private String name;
    private Category category;
    private BigDecimal price;
    private Instant createdDate;
    private Instant modifiedDate;
}
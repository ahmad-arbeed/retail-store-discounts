package com.retail.store.discount.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
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
@Document("bills")
public class Bill {

    @Transient
    public static final String SEQUENCE = "bill_sequence";

    @Id
    private long id;
    private long userId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal netPayableAmount;
    private List<Item> items;
    private Instant createdDate;
}
package com.retail.store.product.model;

import com.retail.store.common.model.DbSequence;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "product_sequence")
public class ProductSequence implements DbSequence {

    @Id
    private String id;
    private long sequence;
}
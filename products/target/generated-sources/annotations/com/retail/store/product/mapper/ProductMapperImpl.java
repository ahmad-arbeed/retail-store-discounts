package com.retail.store.product.mapper;

import com.retail.store.product.dto.ProductDto;
import com.retail.store.product.model.Product;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-31T20:58:38+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product map(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.name( productDto.getName() );
        product.category( productDto.getCategory() );
        product.price( productDto.getPrice() );

        product.createdDate( Instant.now() );

        return product.build();
    }

    @Override
    public ProductDto map(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto.ProductDtoBuilder productDto = ProductDto.builder();

        productDto.id( product.getId() );
        productDto.name( product.getName() );
        productDto.category( product.getCategory() );
        productDto.price( product.getPrice() );
        productDto.createdDate( product.getCreatedDate() );
        productDto.modifiedDate( product.getModifiedDate() );

        return productDto.build();
    }
}

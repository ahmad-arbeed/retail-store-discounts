package com.retail.store.product.mapper;

import com.retail.store.product.dto.ProductDto;
import com.retail.store.product.model.Product;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = Instant.class)
public interface ProductMapper {

    @Mapping(target = "createdDate", expression = "java(Instant.now())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    Product map(ProductDto productDto);

    ProductDto map(Product product);
}

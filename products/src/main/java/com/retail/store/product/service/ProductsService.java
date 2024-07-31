package com.retail.store.product.service;

import com.retail.store.product.dto.ProductDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(long id, ProductDto productDto);

    void deleteProduct(long id);

    ProductDto getProduct(long id);

    Page<ProductDto> getAllProducts(Pageable pageable);

    List<ProductDto> getAllProductsByIds(List<Long> ids);
}

package com.retail.store.product.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.retail.store.common.exception.BusinessException;
import com.retail.store.common.service.SequenceGeneratorServiceImpl;
import com.retail.store.product.constant.Category;
import com.retail.store.product.dto.ProductDto;
import com.retail.store.product.mapper.ProductMapper;
import com.retail.store.product.model.Product;
import com.retail.store.product.repository.ProductRepository;
import com.retail.store.product.service.impl.ProductsServiceImpl;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductsServiceTest {

    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
    SequenceGeneratorServiceImpl sequenceGeneratorService = Mockito.mock(SequenceGeneratorServiceImpl.class);
    ProductsService productService = Mockito.spy(new ProductsServiceImpl(productRepository, productMapper, sequenceGeneratorService));

    @Test
    void testCreateProduct_successResponse() {

        final var dto = ProductDto.builder()
            .name("test")
            .price(new BigDecimal("1500"))
            .category(Category.ELECTRONIC)
            .build();

        final var product = Product.builder()
            .id(1L)
            .name("test")
            .price(new BigDecimal("1500"))
            .category(Category.ELECTRONIC)
            .createdDate(Instant.now())
            .build();

        when(sequenceGeneratorService.generateSequence(any(), any())).then(a -> 1L);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        final var response = productService.createProduct(dto);
        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals("test", response.getName());
        Assertions.assertEquals(Category.ELECTRONIC, response.getCategory());
    }

    @Test
    void testUpdateProduct_successResponse() {

        final var dto = ProductDto.builder()
            .name("test1")
            .price(new BigDecimal("100"))
            .category(Category.ELECTRONIC)
            .build();

        final var product = Product.builder()
            .id(1L)
            .name("test")
            .price(new BigDecimal("1500"))
            .category(Category.ELECTRONIC)
            .createdDate(Instant.now())
            .build();
        final var savedProduct = Product.builder()
            .id(1L)
            .name("test1")
            .price(new BigDecimal("100"))
            .category(Category.ELECTRONIC)
            .createdDate(Instant.now())
            .build();

        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        final var response = productService.updateProduct(1L, dto);
        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals("test1", response.getName());
        Assertions.assertEquals(new BigDecimal("100"), response.getPrice());
        Assertions.assertEquals(Category.ELECTRONIC, response.getCategory());
    }

    @Test
    void testUpdateProductNotFound_badRequest() {

        final var dto = ProductDto.builder()
            .name("test1")
            .price(new BigDecimal("100"))
            .category(Category.ELECTRONIC)
            .build();
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> productService.updateProduct(2L, dto));
    }

    @Test
    void testDeleteProductNotFound_badRequest() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> productService.deleteProduct(2L));
    }

    @Test
    void testGetProductNotFound_badRequest() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> productService.getProduct(2L));
    }

    @Test
    void testGetProductById_successResponse() {
        final var product = Product.builder()
            .id(1L)
            .name("test")
            .price(new BigDecimal("1500"))
            .category(Category.ELECTRONIC)
            .createdDate(Instant.now())
            .build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        final var dto = productService.getProduct(1L);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("test", dto.getName());
        Assertions.assertEquals(new BigDecimal("1500"), dto.getPrice());
        Assertions.assertEquals(Category.ELECTRONIC, dto.getCategory());
    }
}

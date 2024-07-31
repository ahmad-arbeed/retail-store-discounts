package com.retail.store.product.service.impl;

import com.retail.store.common.exception.ApiErrors;
import com.retail.store.common.exception.BusinessException;
import com.retail.store.common.service.SequenceGeneratorService;
import com.retail.store.product.dto.ProductDto;
import com.retail.store.product.mapper.ProductMapper;
import com.retail.store.product.model.Product;
import com.retail.store.product.model.ProductSequence;
import com.retail.store.product.repository.ProductRepository;
import com.retail.store.product.service.ProductsService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductsServiceImpl implements ProductsService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final SequenceGeneratorService generatorService;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Create product {}", productDto);
        final var product = mapper.map(productDto);
        product.setId(generatorService.generateSequence(Product.SEQUENCE, ProductSequence.class));
        return mapper.map(repository.save(product));
    }

    @Override
    public ProductDto updateProduct(long id, ProductDto productDto) {
        log.info("Update product {}", id);
        final var product = repository.findById(id)
            .orElseThrow(() -> new BusinessException(ApiErrors.NOT_FOUND, HttpStatus.BAD_REQUEST, id));

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setModifiedDate(Instant.now());

        return mapper.map(repository.save(product));
    }

    @Override
    public void deleteProduct(long id) {
        log.info("Delete product {}", id);
        if (!repository.existsById(id)) {
            throw new BusinessException(ApiErrors.NOT_FOUND, HttpStatus.BAD_REQUEST, id);
        }
        repository.deleteById(id);
    }

    @Override
    public ProductDto getProduct(long id) {
        log.info("Get product {}", id);
        final var product = repository.findById(id)
            .orElseThrow(() -> new BusinessException(ApiErrors.NOT_FOUND, HttpStatus.BAD_REQUEST, id));
        return mapper.map(product);
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        log.info("Get products pageable {}", pageable);
        return repository.findAll(pageable)
            .map(mapper::map);
    }

    @Override
    public List<ProductDto> getAllProductsByIds(final List<Long> ids) {
        log.info("Get products by ids {}", ids);
        return repository.findAllById(ids)
            .stream()
            .map(mapper::map)
            .toList();
    }
}
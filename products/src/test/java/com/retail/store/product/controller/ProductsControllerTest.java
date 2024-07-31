package com.retail.store.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.retail.store.common.service.SequenceGeneratorServiceImpl;
import com.retail.store.product.constant.Category;
import com.retail.store.product.mapper.ProductMapper;
import com.retail.store.product.model.Product;
import com.retail.store.product.repository.ProductRepository;
import com.retail.store.product.service.impl.ProductsServiceImpl;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ComponentScan("com.retail.store.product.mapper")
@WebMvcTest(ProductsController.class)
class ProductsControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    MongoOperations mongoOperations;
    @MockBean
    ProductRepository productRepository;

    @SpyBean
    ProductsServiceImpl productService;
    @SpyBean
    SequenceGeneratorServiceImpl sequenceGeneratorService;
    @SpyBean
    ProductMapper productMapper;

    @Test
    void testCreateProduct_successResponse() throws Exception {

        final var product = Product.builder()
            .id(1L)
            .name("test")
            .price(new BigDecimal("1500"))
            .category(Category.ELECTRONIC)
            .build();

        when(sequenceGeneratorService.generateSequence(any(), any())).then(a -> 1L);
        when(productRepository.save(any())).then(a -> product);

        final String request = """
            {
                "name": "test",
                "category": "ELECTRONIC",
                "price": 1500
            }
            """;

        final String response = """
            {
                "date": "2024-01-01T00:00:00.000Z",
                "response": {
                    "id": 1,
                    "name": "test",
                    "category": "ELECTRONIC",
                    "price": 1500,
                    "createdDate": "2024-01-01T00:00:00.000Z",
                    "modifiedDate": null
                }
            }
            """;

        final var result = mvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(request))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        assertResponseBody(result, response);
    }

    @Test
    void testUpdateProduct_successResponse() throws Exception {

        final var product = Product.builder()
            .id(1L)
            .name("test")
            .price(new BigDecimal("1500"))
            .category(Category.ELECTRONIC)
            .build();

        when(productRepository.findById(any())).then(a -> Optional.of(product));
        when(productRepository.save(any())).then(a -> product);

        final String request = """
            {
                "name": "test1",
                "category": "ELECTRONIC",
                "price": 500
            }
            """;

        final String response = """
            {
                "date": "2024-01-01T00:00:00.000Z",
                "response": {
                    "id": 1,
                    "name": "test1",
                    "category": "ELECTRONIC",
                    "price": 500,
                    "createdDate": "2024-01-01T00:00:00.000Z",
                    "modifiedDate": null
                }
            }
            """;

        final var result = mvc.perform(MockMvcRequestBuilders.put("/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(request))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        assertResponseBody(result, response);
    }

    @Test
    void testDeleteProduct_successResponse() throws Exception {

        when(productRepository.existsById(any())).then(a -> true);
        doNothing().when(productRepository).deleteById(any());

        mvc.perform(MockMvcRequestBuilders.delete("/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent())
            .andReturn();
    }

    @Test
    void testGetProductById_successResponse() throws Exception {

        final var product = Product.builder()
            .id(1L)
            .name("test")
            .price(new BigDecimal("1500"))
            .category(Category.ELECTRONIC)
            .build();

        when(productRepository.findById(any())).then(a -> Optional.of(product));

        final String response = """
            {
                "date": "2024-01-01T00:00:00.000Z",
                "response": {
                    "id": 1,
                    "name": "test",
                    "category": "ELECTRONIC",
                    "price": 1500,
                    "createdDate": "2024-01-01T00:00:00.000Z",
                    "modifiedDate": null
                }
            }
            """;

        final var result = mvc.perform(MockMvcRequestBuilders.get("/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        assertResponseBody(result, response);
    }

    @Test
    void testGetAllProductsPaginated_successResponse() throws Exception {

        final var product1 = Product.builder()
            .id(1L)
            .name("test")
            .price(new BigDecimal("1500"))
            .category(Category.ELECTRONIC)
            .build();

        when(productRepository.findAll(any(Pageable.class))).then(a -> new PageImpl<>(List.of(product1)));

        var response = """
            {
                "date": "2024-07-30T16:22:47.004695Z",
                "response": {
                    "content": [
                        {
                            "id": 1,
                            "name": "test",
                            "category": "ELECTRONIC",
                            "price": 1500,
                            "createdDate": "2024-07-29T21:34:42.440Z",
                            "modifiedDate": null
                        }
                    ],
                    "page": {
                        "size": 1,
                        "number": 0,
                        "totalElements": 1,
                        "totalPages": 1
                    }
                }
            }
            """;

        var result = mvc.perform(MockMvcRequestBuilders.get("/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .param("page", "0")
                .param("size", "1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        JsonAssertions.assertThatJson(result.getResponse().getContentAsString())
            .whenIgnoringPaths("date", "response.content[0].createdDate")
            .isEqualTo(response);
    }

    @Test
    void testGetAllProductsNotPaginated_successResponse() throws Exception {

        final var product = Product.builder()
            .id(1L)
            .name("test")
            .price(new BigDecimal("1500"))
            .category(Category.ELECTRONIC)
            .build();

        when(productRepository.findAll(any(Pageable.class))).then(a -> new PageImpl<>(List.of(product)));

        final String response = """
            {
                "date": "2024-07-30T16:22:47.004695Z",
                "response": {
                    "content": [
                        {
                            "id": 1,
                            "name": "test",
                            "category": "ELECTRONIC",
                            "price": 1500,
                            "createdDate": "2024-07-29T21:34:42.440Z",
                            "modifiedDate": null
                        }
                    ],
                    "page": {
                        "size": 1,
                        "number": 0,
                        "totalElements": 1,
                        "totalPages": 1
                    }
                }
            }
            """;

        final var result = mvc.perform(MockMvcRequestBuilders.get("/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .param("page", "-1")
                .param("size", "-1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        JsonAssertions.assertThatJson(result.getResponse().getContentAsString())
            .whenIgnoringPaths("date", "response.content[0].createdDate", "response.content[0].modifiedDate")
            .isEqualTo(response);
    }

    @Test
    void testGetAllProductsByIds_successResponse() throws Exception {

        final var product = Product.builder()
            .id(1L)
            .name("test")
            .price(new BigDecimal("1500"))
            .category(Category.ELECTRONIC)
            .build();

        when(productRepository.findAllById(any())).then(a -> List.of(product));

        final String response = """
            {
                "date": "2024-07-30T16:22:47.004695Z",
                "response": [
                    {
                        "id": 1,
                        "name": "test",
                        "category": "ELECTRONIC",
                        "price": 1500,
                        "createdDate": "2024-07-29T21:34:42.440Z",
                        "modifiedDate": null
                    }
                ]
            }
            """;

        final var result = mvc.perform(MockMvcRequestBuilders.get("/all")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .param("ids", "1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        JsonAssertions.assertThatJson(result.getResponse().getContentAsString())
            .whenIgnoringPaths("date", "response[0].createdDate", "response[0].modifiedDate")
            .isEqualTo(response);
    }


    private void assertResponseBody(MvcResult result, String response) throws UnsupportedEncodingException {
        JsonAssertions.assertThatJson(result.getResponse().getContentAsString())
            .whenIgnoringPaths("date", "response.createdDate", "response.modifiedDate")
            .isEqualTo(response);
    }
}

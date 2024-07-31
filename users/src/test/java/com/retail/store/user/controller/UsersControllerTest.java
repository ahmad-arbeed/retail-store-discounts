package com.retail.store.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.retail.store.common.service.SequenceGeneratorServiceImpl;
import com.retail.store.user.constant.UserType;
import com.retail.store.user.mapper.UserMapper;
import com.retail.store.user.model.User;
import com.retail.store.user.repository.UserRepository;
import com.retail.store.user.service.impl.UsersServiceImpl;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
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

@ComponentScan("com.retail.store.user.mapper")
@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    MongoOperations mongoOperations;
    @MockBean
    UserRepository userRepository;

    @SpyBean
    UsersServiceImpl userService;
    @SpyBean
    SequenceGeneratorServiceImpl sequenceGeneratorService;
    @SpyBean
    UserMapper userMapper;

    @Test
    void testCreateUser_successResponse() throws Exception {

        final var user = User.builder()
            .id(1L)
            .name("test")
            .userType(UserType.EMPLOYEE)
            .createdDate(Instant.now())
            .build();

        when(sequenceGeneratorService.generateSequence(any(), any())).then(a -> 1L);
        when(userRepository.save(any())).then(a -> user);

        final String request = """
            {
                "name": "test",
                "userType": "EMPLOYEE"
            }
            """;

        final String response = """
            {
                "date": "2024-01-01T00:00:00.000Z",
                "response": {
                    "id": 1,
                    "name": "test",
                    "userType": "EMPLOYEE",
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
    void testUpdateUser_successResponse() throws Exception {

        final var user = User.builder()
            .id(1L)
            .name("test")
            .userType(UserType.EMPLOYEE)
            .build();

        when(userRepository.findById(any())).then(a -> Optional.of(user));
        when(userRepository.save(any())).then(a -> user);

        final String request = """
            {
                "name": "test1",
                "userType": "EMPLOYEE"
            }
            """;

        final String response = """
            {
                "date": "2024-01-01T00:00:00.000Z",
                "response": {
                    "id": 1,
                    "name": "test1",
                    "userType": "EMPLOYEE",
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
    void testDeleteUser_successResponse() throws Exception {

        when(userRepository.existsById(any())).then(a -> true);
        doNothing().when(userRepository).deleteById(any());

        mvc.perform(MockMvcRequestBuilders.delete("/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent())
            .andReturn();
    }

    @Test
    void testGetUserById_successResponse() throws Exception {

        final var user = User.builder()
            .id(1L)
            .name("test")
            .userType(UserType.EMPLOYEE)
            .build();

        when(userRepository.findById(any())).then(a -> Optional.of(user));

        final String response = """
            {
                "date": "2024-01-01T00:00:00.000Z",
                "response": {
                    "id": 1,
                    "name": "test",
                    "userType": "EMPLOYEE",
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
    void testGetAllUserPaginated_successResponse() throws Exception {

        final var user1 = User.builder()
            .id(1L)
            .name("test")
            .userType(UserType.EMPLOYEE)
            .build();

        when(userRepository.findAll(any(Pageable.class))).then(a -> new PageImpl<>(List.of(user1)));

        var response = """
            {
                "date": "2024-07-30T16:22:47.004695Z",
                "response": {
                    "content": [
                        {
                            "id": 1,
                            "name": "test",
                            "userType": "EMPLOYEE",
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
    void testGetAllUserNotPaginated_successResponse() throws Exception {

        final var user = User.builder()
            .id(1L)
            .name("test")
            .userType(UserType.EMPLOYEE)
            .build();

        when(userRepository.findAll(any(Pageable.class))).then(a -> new PageImpl<>(List.of(user)));

        final String response = """
            {
                "date": "2024-07-30T16:22:47.004695Z",
                "response": {
                    "content": [
                        {
                            "id": 1,
                            "name": "test",
                            "userType": "EMPLOYEE",
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

    private void assertResponseBody(MvcResult result, String response) throws UnsupportedEncodingException {
        JsonAssertions.assertThatJson(result.getResponse().getContentAsString())
            .whenIgnoringPaths("date", "response.createdDate", "response.modifiedDate")
            .isEqualTo(response);
    }
}

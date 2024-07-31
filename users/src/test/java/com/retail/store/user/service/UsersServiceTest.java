package com.retail.store.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.retail.store.common.exception.BusinessException;
import com.retail.store.common.service.SequenceGeneratorServiceImpl;
import com.retail.store.user.constant.UserType;
import com.retail.store.user.dto.UserDto;
import com.retail.store.user.mapper.UserMapper;
import com.retail.store.user.model.User;
import com.retail.store.user.repository.UserRepository;
import com.retail.store.user.service.impl.UsersServiceImpl;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    SequenceGeneratorServiceImpl sequenceGeneratorService = Mockito.mock(SequenceGeneratorServiceImpl.class);
    UsersService userService = Mockito.spy(new UsersServiceImpl(userRepository, userMapper, sequenceGeneratorService));

    @Test
    void testCreateUser_successResponse() {

        final var dto = UserDto.builder()
            .name("Ahmad")
            .userType(UserType.EMPLOYEE)
            .build();

        final var user = User.builder()
            .id(1L)
            .name("Ahmad")
            .userType(UserType.EMPLOYEE)
            .createdDate(Instant.now())
            .build();

        when(sequenceGeneratorService.generateSequence(any(), any())).then(a -> 1L);
        when(userRepository.save(any(User.class))).thenReturn(user);

        final var response = userService.createUser(dto);
        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals("Ahmad", response.getName());
        Assertions.assertEquals(UserType.EMPLOYEE, response.getUserType());
    }

    @Test
    void testUpdateUser_successResponse() {

        final var dto = UserDto.builder()
            .name("test1")
            .userType(UserType.EMPLOYEE)
            .build();

        final var user = User.builder()
            .id(1L)
            .name("Ahmad")
            .userType(UserType.EMPLOYEE)
            .createdDate(Instant.now())
            .build();
        final var savedUser = User.builder()
            .id(1L)
            .name("test1")
            .userType(UserType.EMPLOYEE)
            .createdDate(Instant.now())
            .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        final var response = userService.updateUser(1L, dto);
        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals("test1", response.getName());
        Assertions.assertEquals(UserType.EMPLOYEE, response.getUserType());
    }

    @Test
    void testUpdateUserNotFound_badRequest() {

        final var dto = UserDto.builder()
            .name("test1")
            .userType(UserType.EMPLOYEE)
            .build();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> userService.updateUser(2L, dto));
    }

    @Test
    void testDeleteUserNotFound_badRequest() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> userService.deleteUser(2L));
    }

    @Test
    void testGetUserNotFound_badRequest() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> userService.getUser(2L));
    }

    @Test
    void testGetUserById_successResponse() {
        final var user = User.builder()
            .id(1L)
            .name("Ahmad")
            .userType(UserType.EMPLOYEE)
            .createdDate(Instant.now())
            .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        final var dto = userService.getUser(1L);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("Ahmad", dto.getName());
        Assertions.assertEquals(UserType.EMPLOYEE, dto.getUserType());
    }
}
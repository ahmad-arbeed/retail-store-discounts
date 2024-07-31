package com.retail.store.user.service;

import com.retail.store.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsersService {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(long id, UserDto userDto);

    void deleteUser(long id);

    UserDto getUser(long id);

    Page<UserDto> getAllUsers(Pageable pageable);
}
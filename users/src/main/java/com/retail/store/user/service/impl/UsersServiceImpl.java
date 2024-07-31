package com.retail.store.user.service.impl;

import com.retail.store.common.exception.ApiErrors;
import com.retail.store.common.exception.BusinessException;
import com.retail.store.common.service.SequenceGeneratorService;
import com.retail.store.user.dto.UserDto;
import com.retail.store.user.mapper.UserMapper;
import com.retail.store.user.model.User;
import com.retail.store.user.model.UserSequence;
import com.retail.store.user.repository.UserRepository;
import com.retail.store.user.service.UsersService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final SequenceGeneratorService generatorService;

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Create user {}", userDto);
        final var user = mapper.map(userDto);
        user.setId(generatorService.generateSequence(User.SEQUENCE, UserSequence.class));
        return mapper.map(repository.save(user));
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        log.info("Update user {}", id);
        final var user = repository.findById(id)
            .orElseThrow(() -> new BusinessException(ApiErrors.NOT_FOUND, HttpStatus.BAD_REQUEST, id));

        user.setName(userDto.getName());
        user.setUserType(userDto.getUserType());
        user.setModifiedDate(Instant.now());

        return mapper.map(repository.save(user));
    }

    @Override
    public void deleteUser(long id) {
        log.info("Delete user {}", id);
        if (!repository.existsById(id)) {
            throw new BusinessException(ApiErrors.NOT_FOUND, HttpStatus.BAD_REQUEST, id);
        }
        repository.deleteById(id);
    }

    @Override
    public UserDto getUser(long id) {
        log.info("Get user {}", id);
        final var user = repository.findById(id)
            .orElseThrow(() -> new BusinessException(ApiErrors.NOT_FOUND, HttpStatus.BAD_REQUEST, id));
        return mapper.map(user);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        log.info("Get users pageable {}", pageable);
        return repository.findAll(pageable)
            .map(mapper::map);
    }
}
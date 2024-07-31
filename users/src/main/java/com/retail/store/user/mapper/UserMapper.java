package com.retail.store.user.mapper;

import com.retail.store.user.dto.UserDto;
import com.retail.store.user.model.User;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = Instant.class)
public interface UserMapper {

    @Mapping(target = "createdDate", expression = "java(Instant.now())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    User map(UserDto userDto);

    UserDto map(User product);
}
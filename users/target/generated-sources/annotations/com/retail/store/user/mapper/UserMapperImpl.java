package com.retail.store.user.mapper;

import com.retail.store.user.dto.UserDto;
import com.retail.store.user.model.User;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-31T21:01:48+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User map(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( userDto.getName() );
        user.userType( userDto.getUserType() );

        user.createdDate( Instant.now() );

        return user.build();
    }

    @Override
    public UserDto map(User product) {
        if ( product == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( product.getId() );
        userDto.name( product.getName() );
        userDto.userType( product.getUserType() );
        userDto.createdDate( product.getCreatedDate() );
        userDto.modifiedDate( product.getModifiedDate() );

        return userDto.build();
    }
}

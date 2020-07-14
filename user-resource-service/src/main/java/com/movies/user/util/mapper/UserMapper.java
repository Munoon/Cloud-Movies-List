package com.movies.user.util.mapper;

import com.movies.user.user.User;
import com.movies.user.user.to.RegisterUserTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", source = "password")
    @Mapping(target = "roles", expression = "java(java.util.Collections.singleton(com.movies.user.user.UserRoles.ROLE_USER))")
    @Mapping(target = "registered", expression = "java(java.time.LocalDateTime.now())")
    User toUser(RegisterUserTo registerUserTo, String password);
}

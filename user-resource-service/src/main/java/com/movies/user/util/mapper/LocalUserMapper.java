package com.movies.user.util.mapper;

import com.movies.common.user.User;
import com.movies.user.user.UserEntity;
import com.movies.user.user.to.RegisterUserTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocalUserMapper {
    LocalUserMapper INSTANCE = Mappers.getMapper(LocalUserMapper.class);

    @Mapping(target = "password", source = "password")
    @Mapping(target = "roles", expression = "java(java.util.Collections.singleton(com.movies.common.user.UserRoles.ROLE_USER))")
    @Mapping(target = "registered", expression = "java(java.time.LocalDateTime.now())")
    UserEntity toUserEntity(RegisterUserTo registerUserTo, String password);

    User asUser(UserEntity userEntity);
}

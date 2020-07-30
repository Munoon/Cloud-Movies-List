package com.movies.user.util.mapper;

import com.movies.common.user.User;
import com.movies.common.user.UserTo;
import com.movies.user.user.UserEntity;
import com.movies.user.user.to.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper
public interface LocalUserMapper {
    LocalUserMapper INSTANCE = Mappers.getMapper(LocalUserMapper.class);

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(registerUserTo.getPassword()))")
    @Mapping(target = "email", expression = "java(registerUserTo.getEmail().toLowerCase())")
    @Mapping(target = "roles", expression = "java(java.util.Collections.singleton(com.movies.common.user.UserRoles.ROLE_USER))")
    @Mapping(target = "registered", expression = "java(java.time.LocalDateTime.now())")
    UserEntity toUserEntity(RegisterUserTo registerUserTo, PasswordEncoder passwordEncoder);

    User asUser(UserEntity userEntity);

    UserEntity updateEntity(UpdateProfileTo updateProfileTo, @MappingTarget UserEntity userEntity);

    @Mapping(target = "email", expression = "java(updateEmailTo.getEmail().toLowerCase())")
    UserEntity updateEntity(UpdateEmailTo updateEmailTo, @MappingTarget UserEntity userEntity);

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(updatePasswordTo.getNewPassword()))")
    UserEntity updateEntity(UpdatePasswordTo updatePasswordTo, @MappingTarget UserEntity userEntity, PasswordEncoder passwordEncoder);

    UserToRepresentationModel toRepresentationModel(UserTo userTo);
}

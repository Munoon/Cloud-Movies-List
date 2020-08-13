package com.movies.auth.user;

import com.movies.common.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocalUserMapper {
    LocalUserMapper INSTANCE = Mappers.getMapper(LocalUserMapper.class);

    User asUser(UserEntity userEntity);
}

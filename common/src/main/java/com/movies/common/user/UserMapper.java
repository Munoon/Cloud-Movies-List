package com.movies.common.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserTo asTo(User user);

    default UserTo asTo(Map<String, Object> parameters) {
        UserTo userTo = new UserTo();
        userTo.setId(Integer.valueOf((String) parameters.get("id")));
        userTo.setName((String) parameters.get("name"));
        userTo.setSurname((String) parameters.get("surname"));
        userTo.setEmail((String) parameters.get("email"));
        return userTo;
    }
}

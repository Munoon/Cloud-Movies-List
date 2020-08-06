package com.movies.common.user;

import com.movies.common.util.UserUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserTo asTo(User user);

    default UserTo asTo(Map<String, Object> parameters) {
        UserTo userTo = new UserTo();
        userTo.setId(Integer.parseInt(String.valueOf(parameters.get("id"))));
        userTo.setName((String) parameters.get("name"));
        userTo.setSurname((String) parameters.get("surname"));
        userTo.setEmail((String) parameters.get("email"));

        List<UserRoles> userRolesList = UserUtils.getUserRoles(parameters);
        userTo.setRoles(Set.copyOf(userRolesList));

        return userTo;
    }

    default Map<String, ?> asMap(UserTo userTo) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", userTo.getId());
        result.put("name", userTo.getName());
        result.put("surname", userTo.getSurname());
        result.put("email", userTo.getEmail());
        result.put("roles", userTo.getRoles());
        return result;
    }
}

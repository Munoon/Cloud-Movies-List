package com.movies.common.util;

import com.movies.common.user.UserRoles;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserUtils {
    public static String getPasswordFromMap(Map<String, Object> params) {
        String password = (String) params.get("password");
        return Optional.ofNullable(password).orElse("N/A");
    }

    public static List<UserRoles> getUserRoles(Map<String, Object> params) {
        Iterable<String> roles = (Iterable<String>) params.get("roles");
        return StreamSupport.stream(roles.spliterator(), false)
                .map(UserRoles::valueOf)
                .collect(Collectors.toList());
    }
}

package com.movies.user.util;

import com.movies.user.user.UserService;

import javax.validation.ValidationException;
import java.util.Map;

public class UserUtils {
    private static final String PASSWORD_FIELD_NAME = "password";

    public static void validatePassword(Map<String, Object> payload, int userId, UserService userService) {
        if (!payload.containsKey(PASSWORD_FIELD_NAME) || !(payload.get(PASSWORD_FIELD_NAME) instanceof String)) {
            throw new ValidationException("Password not provided or incorrect type");
        }

        String password = (String) payload.get(PASSWORD_FIELD_NAME);
        validatePassword(password, userId, userService);
    }


    public static void validatePassword(String password, int userId, UserService userService) {
        boolean result = userService.testPassword(userId, password);
        if (!result) {
            throw new ValidationException("Password invalid!");
        }
    }
}

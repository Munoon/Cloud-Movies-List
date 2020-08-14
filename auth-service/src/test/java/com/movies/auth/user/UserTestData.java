package com.movies.auth.user;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;

import java.util.Set;

public class UserTestData {
    public static final int DEFAULT_USER_ID = 100;
    public static final String DEFAULT_USER_EMAIL = "munoongg@gmail.com";
    public static final User DEFAULT_USER = new User(DEFAULT_USER_ID, "Nikita", "Ivchenko", DEFAULT_USER_EMAIL, "{noop}pass", null, Set.of(UserRoles.ROLE_ADMIN, UserRoles.ROLE_USER));
}

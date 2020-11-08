package com.movies.auth.user;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestData {
    public static final int DEFAULT_USER_ID = 100;
    public static final String DEFAULT_USER_EMAIL = "munoongg@gmail.com";
    public static final User DEFAULT_USER = new User(DEFAULT_USER_ID, "Nikita", "Ivchenko", DEFAULT_USER_EMAIL, "{noop}password", null, Set.of(UserRoles.ROLE_ADMIN, UserRoles.ROLE_USER));

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "password", "registered");
    }
}

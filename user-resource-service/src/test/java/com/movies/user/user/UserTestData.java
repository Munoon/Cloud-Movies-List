package com.movies.user.user;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import com.movies.common.user.UserTo;
import com.movies.user.util.JsonUtil;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserTestData {
    public static final int DEFAULT_USER_ID = 100;
    public static final String DEFAULT_USER_EMAIL = "munoongg@gmail.com";
    public static final User DEFAULT_USER = new User(DEFAULT_USER_ID, "Nikita", "Ivchenko", DEFAULT_USER_EMAIL, "{noop}pass", null, Set.of(UserRoles.ROLE_ADMIN, UserRoles.ROLE_USER));

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "password", "registered");
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("password", "registered").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatchIgnoreId(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "password", "registered", "id");
    }

    public static void assertMatchIgnoreId(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("password", "registered", "id").isEqualTo(expected);
    }

    public static void assertMatchIgnoreId(Iterable<User> actual, User... expected) {
        assertMatchIgnoreId(actual, Arrays.asList(expected));
    }

    private static void assertMatch(UserTo actual, UserTo expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static ResultMatcher contentJson(User expected) {
        return result -> assertMatch(JsonUtil.readFromJson(result, User.class), expected);
    }

    public static ResultMatcher contentJson(UserTo expected) {
        return result -> assertMatch(JsonUtil.readFromJson(result, UserTo.class), expected);
    }
}

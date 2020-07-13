package com.movies.user.user;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserTestData {
    public static final User DEFAULT_USER = new User(100, "Nikita", "Ivchenko", "munoongg@gmail.com", "{noop}pass", null);

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("password", "registered").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatchIgnoreId(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("password", "registered", "id").isEqualTo(expected);
    }

    public static void assertMatchIgnoreId(Iterable<User> actual, User... expected) {
        assertMatchIgnoreId(actual, Arrays.asList(expected));
    }
}

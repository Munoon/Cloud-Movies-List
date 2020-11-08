package com.movies.user.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.movies.common.user.User;
import com.movies.common.user.UserMapper;
import com.movies.common.user.UserRoles;
import com.movies.common.user.UserTo;
import com.movies.user.config.MvcConfig;
import com.movies.user.util.JsonUtil;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserTestData {
    public static final int DEFAULT_USER_ID = 100;
    public static final String DEFAULT_USER_EMAIL = "munoongg@gmail.com";
    public static final User DEFAULT_USER = new User(DEFAULT_USER_ID, "Nikita", "Ivchenko", DEFAULT_USER_EMAIL, "{noop}password", null, Set.of(UserRoles.ROLE_ADMIN, UserRoles.ROLE_USER));

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

    public static ResultMatcher contentJsonHateos(User... expected) {
        UserTo[] expectedUserTos = Stream.of(expected)
                .map(UserMapper.INSTANCE::asTo)
                .toArray(UserTo[]::new);
        return contentJsonHateos(expectedUserTos);
    }

    public static ResultMatcher contentJsonHateos(UserTo... expected) {
        return result -> {
            String content = JsonUtil.getContent(result);
            JsonNode node = MvcConfig.OBJECT_MAPPER.readTree(content).at("/_embedded/users");
            List<UserTo> actual = MvcConfig.OBJECT_MAPPER.convertValue(node, new TypeReference<>() {});
            assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(List.of(expected));
        };
    }

    public static ResultMatcher contentJson(User expected) {
        return contentJson(UserMapper.INSTANCE.asTo(expected));
    }

    public static ResultMatcher contentJson(UserTo expected) {
        return result -> assertMatch(JsonUtil.readFromJson(result, UserTo.class), expected);
    }
}

package com.movies.user.user;

import com.movies.common.user.User;
import com.movies.common.user.UserMapper;
import com.movies.common.user.UserRoles;
import com.movies.common.user.UserTo;
import com.movies.user.util.JsonUtil;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    public static ResultMatcher contentJsonHateos(User... expected) {
        return result -> {
            Map<String, Object> resultMap = JsonUtil.readValueAsMap(JsonUtil.getContent(result));
            List<Map<String, Object>> maps = ((Map<String, List<Map<String, Object>>>) resultMap.get("_embedded")).get("users");

            assertThat(maps).hasSize(expected.length);

            // TODO refactor validation comparing each field
            for (int i = 0; i < expected.length; i++) {
                User expectedUser = expected[i];
                assertThat(expectedUser.getId()).isEqualTo(maps.get(i).get("id"));
            }
        };
    }

    public static ResultMatcher contentJson(User expected) {
        return contentJson(UserMapper.INSTANCE.asTo(expected));
    }

    public static ResultMatcher contentJson(UserTo expected) {
        return result -> assertMatch(JsonUtil.readFromJson(result, UserTo.class), expected);
    }
}

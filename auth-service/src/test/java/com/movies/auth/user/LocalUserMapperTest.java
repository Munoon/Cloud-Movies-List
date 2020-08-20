package com.movies.auth.user;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.movies.auth.user.UserTestData.assertMatch;

class LocalUserMapperTest {
    @Test
    void asUser() {
        var entity = new UserEntity(100, "Test", "User", "test@email.com", "pass", null, Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));
        var user = LocalUserMapper.INSTANCE.asUser(entity);
        var expected = new User(100, "Test", "User", "test@email.com", "pass", null, Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));
        assertMatch(user, expected);
    }
}
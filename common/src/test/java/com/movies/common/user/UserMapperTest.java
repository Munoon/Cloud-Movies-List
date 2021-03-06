package com.movies.common.user;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    @Test
    void asTo() {
        User user = new User(100500, "Test", "Surname", "email@example.com", null, null, Collections.singleton(UserRoles.ROLE_USER));
        UserTo userTo = UserMapper.INSTANCE.asTo(user);

        assertThat(userTo.getId()).isEqualTo(100500);
        assertThat(userTo.getName()).isEqualTo("Test");
        assertThat(userTo.getSurname()).isEqualTo("Surname");
        assertThat(userTo.getEmail()).isEqualTo("email@example.com");
        assertThat(userTo.getRoles()).isEqualTo(Collections.singleton(UserRoles.ROLE_USER));
    }

    @Test
    void asToFromMap() {
        UserTo userTo = UserMapper.INSTANCE.asTo(Map.of(
                "id", 100500,
                "name", "Test",
                "surname", "Surname",
                "email", "test@example.com",
                "roles", Arrays.asList("ROLE_USER", "ROLE_ADMIN")
        ));

        assertThat(userTo.getId()).isEqualTo(100500);
        assertThat(userTo.getName()).isEqualTo("Test");
        assertThat(userTo.getSurname()).isEqualTo("Surname");
        assertThat(userTo.getEmail()).isEqualTo("test@example.com");
        assertThat(userTo.getRoles()).isEqualTo(Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));
    }
}
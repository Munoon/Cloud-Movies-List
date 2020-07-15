package com.movies.user.util.mapper;

import com.movies.user.user.User;
import com.movies.user.user.UserRoles;
import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.user.to.UserTo;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    @Test
    void toUser() {
        RegisterUserTo registerUserTo = new RegisterUserTo();
        registerUserTo.setEmail("TestEmail");
        registerUserTo.setName("New");
        registerUserTo.setSurname("User");
        registerUserTo.setPassword("123");

        User user = UserMapper.INSTANCE.toUser(registerUserTo, "456");
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("New");
        assertThat(user.getSurname()).isEqualTo("User");
        assertThat(user.getEmail()).isEqualTo("TestEmail");
        assertThat(user.getPassword()).isEqualTo("456");
        assertThat(user.getRegistered()).isNotNull();
    }

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
}
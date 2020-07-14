package com.movies.user.util.mapper;

import com.movies.user.user.User;
import com.movies.user.user.to.RegisterUserTo;
import org.junit.jupiter.api.Test;

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
}
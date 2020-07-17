package com.movies.user.util.mapper;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import com.movies.user.user.UserEntity;
import com.movies.user.user.to.RegisterUserTo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    @Test
    void toUserEntity() {
        RegisterUserTo registerUserTo = new RegisterUserTo();
        registerUserTo.setEmail("TestEmail");
        registerUserTo.setName("New");
        registerUserTo.setSurname("User");
        registerUserTo.setPassword("123");

        UserEntity user = LocalUserMapper.INSTANCE.toUserEntity(registerUserTo, "456");
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("New");
        assertThat(user.getSurname()).isEqualTo("User");
        assertThat(user.getEmail()).isEqualTo("TestEmail");
        assertThat(user.getPassword()).isEqualTo("456");
        assertThat(user.getRegistered()).isNotNull();
    }

    @Test
    void asUser() {
        UserEntity userEntity = new UserEntity(100, "Test", "User", "email@example.com", "password", LocalDateTime.of(2019, 12, 27, 13, 0), Collections.singleton(UserRoles.ROLE_USER));
        User user = LocalUserMapper.INSTANCE.asUser(userEntity);
        assertThat(user.getId()).isEqualTo(100);
        assertThat(user.getName()).isEqualTo("Test");
        assertThat(user.getSurname()).isEqualTo("User");
        assertThat(user.getEmail()).isEqualTo("email@example.com");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getRegistered()).isEqualTo(LocalDateTime.of(2019, 12, 27, 13, 0));
        assertThat(user.getRoles()).isEqualTo(Collections.singleton(UserRoles.ROLE_USER));
    }
}
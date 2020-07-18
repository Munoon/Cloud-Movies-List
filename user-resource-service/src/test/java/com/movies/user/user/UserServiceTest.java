package com.movies.user.user;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import com.movies.user.AbstractTest;
import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.util.exception.NotFoundException;
import com.movies.user.util.mapper.LocalUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.movies.user.user.UserTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceTest extends AbstractTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUser() {
        RegisterUserTo registerUserTo = new RegisterUserTo();
        registerUserTo.setName("New");
        registerUserTo.setSurname("User");
        registerUserTo.setEmail("test@example.com");
        registerUserTo.setPassword("examplePassword");

        User savedUser = userService.registerUser(registerUserTo);

        User expected = new User(savedUser.getId(), "New", "User", "test@example.com", null, null, Collections.singleton(UserRoles.ROLE_USER));
        List<User> allUsers = userRepository.findAll()
                .stream()
                .map(LocalUserMapper.INSTANCE::asUser)
                .collect(Collectors.toList());
        assertMatch(allUsers, DEFAULT_USER, expected);
    }

    @Test
    void getByEmail() {
        User actual = userService.getByEmail(DEFAULT_USER.getEmail());
        assertMatch(actual, DEFAULT_USER);
    }

    @Test
    void getByEmailNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getByEmail("unknonwEmail@example.com"));
    }

    @Test
    void testEmail() {
        assertThat(userService.testEmail(DEFAULT_USER_EMAIL)).isEqualTo(false);
        assertThat(userService.testEmail("email@example.com")).isEqualTo(true);
    }
}
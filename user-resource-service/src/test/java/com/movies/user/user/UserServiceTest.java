package com.movies.user.user;

import com.movies.user.AbstractTest;
import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static com.movies.user.user.UserTestData.DEFAULT_USER;
import static com.movies.user.user.UserTestData.assertMatch;
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
        assertMatch(userRepository.findAll(), DEFAULT_USER, expected);
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
}
package com.movies.user.user;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import com.movies.user.AbstractTest;
import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.user.to.UpdateEmailTo;
import com.movies.user.user.to.UpdatePasswordTo;
import com.movies.user.user.to.UpdateProfileTo;
import com.movies.user.util.exception.NotFoundException;
import com.movies.user.util.mapper.LocalUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.movies.user.user.UserTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceTest extends AbstractTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        User newUser = userService.getById(savedUser.getId());
        assertTrue(passwordEncoder.matches("examplePassword", newUser.getPassword()));
    }

    @Test
    void getByEmail() {
        User actual = userService.getByEmail(DEFAULT_USER_EMAIL);
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

    @Test
    void getById() {
        User actual = userService.getById(DEFAULT_USER_ID);
        assertMatch(actual, DEFAULT_USER);
    }

    @Test
    void getByIdNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getById(100500));
    }

    @Test
    void updateUserProfile() {
        UpdateProfileTo updateProfileTo = new UpdateProfileTo();
        updateProfileTo.setName("NewName");
        updateProfileTo.setSurname("NewSurname");

        userService.updateUser(DEFAULT_USER_ID, updateProfileTo);

        User actual = userService.getById(DEFAULT_USER_ID);
        User expected = new User(DEFAULT_USER);
        expected.setName("NewName");
        expected.setSurname("NewSurname");

        assertMatch(actual, expected);
    }

    @Test
    void updateUserEmail() {
        UpdateEmailTo updateEmailTo = new UpdateEmailTo();
        updateEmailTo.setEmail("newEmail@example.com");

        userService.updateUser(DEFAULT_USER_ID, updateEmailTo);

        User actual = userService.getById(DEFAULT_USER_ID);
        User expected = new User(DEFAULT_USER);
        expected.setEmail("newemail@example.com");

        assertMatch(actual, expected);
    }

    @Test
    void updateUserPassword() {
        UpdatePasswordTo updatePasswordTo = new UpdatePasswordTo();
        updatePasswordTo.setOldPassword("password1");
        updatePasswordTo.setNewPassword("password2");

        userService.updateUser(DEFAULT_USER_ID, updatePasswordTo);

        User actual = userService.getById(DEFAULT_USER_ID);
        assertTrue(passwordEncoder.matches("password2", actual.getPassword()));
    }
}
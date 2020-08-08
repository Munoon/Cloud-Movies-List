package com.movies.user.user;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import com.movies.user.AbstractTest;
import com.movies.user.user.to.*;
import com.movies.user.util.exception.NotFoundException;
import com.movies.user.util.mapper.LocalUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Set;
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
        registerUserTo.setEmail("Test@example.com");
        registerUserTo.setPassword("examplePassword");

        User savedUser = userService.createUser(registerUserTo);

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
    void createUser() {
        AdminCreateUserTo adminCreateUserTo = new AdminCreateUserTo();
        adminCreateUserTo.setName("New");
        adminCreateUserTo.setSurname("User");
        adminCreateUserTo.setEmail("Test@example.com");
        adminCreateUserTo.setPassword("examplePassword");
        adminCreateUserTo.setRoles(Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));
        User savedUser = userService.createUser(adminCreateUserTo);

        User expected = new User(savedUser.getId(), "New", "User", "test@example.com", null, null, Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));
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

    @Test
    void updateUserAdmin() {
        AdminUpdateUserTo adminUpdateUserTo = new AdminUpdateUserTo();
        adminUpdateUserTo.setEmail("example@example.com");
        adminUpdateUserTo.setName("NewName");
        adminUpdateUserTo.setSurname("NewSurname");
        adminUpdateUserTo.setRoles(Collections.singleton(UserRoles.ROLE_USER));

        userService.updateUser(DEFAULT_USER_ID, adminUpdateUserTo);

        User actual = userService.getById(DEFAULT_USER_ID);
        User expected = new User(DEFAULT_USER);
        expected.setEmail("example@example.com");
        expected.setName("NewName");
        expected.setSurname("NewSurname");
        expected.setRoles(Collections.singleton(UserRoles.ROLE_USER));
        assertMatch(actual, expected);
    }

    @Test
    void deleteUserById() {
        Page<User> users = userService.findAll(PageRequest.of(0, 10));
        assertThat(users).hasSize(1);

        userService.deleteUserById(DEFAULT_USER_ID);

        Page<User> newUsers = userService.findAll(PageRequest.of(0, 10));
        assertThat(newUsers).hasSize(0);
    }

    @Test
    void findAll() {
        PageRequest page = PageRequest.of(0, 1);
        Page<User> users = userService.findAll(page);

        assertThat(users.getTotalPages()).isEqualTo(1);
        assertThat(users.getTotalElements()).isEqualTo(1);
        assertMatch(users.getContent(), DEFAULT_USER);
    }

    @Test
    void testPassword() {
        assertThat(userService.testPassword(DEFAULT_USER_ID, "pass")).isTrue();
        assertThat(userService.testPassword(DEFAULT_USER_ID, "unCorrectPass")).isFalse();
    }
}
package com.movies.user.user;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import com.movies.user.AbstractTest;
import com.movies.user.user.to.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import static com.movies.user.user.UserTestData.*;
import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

class UserServiceCacheTest extends AbstractTest {
    @Autowired
    private UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void registerUser() {
        var register = new RegisterUserTo();
        register.setName("Test");
        register.setSurname("Test");
        register.setEmail("test@example.com");
        register.setPassword("password");
        User user = service.createUser(register);

        assertMatch(getCache(user.getId()), user);
        assertMatch(getCache(user.getEmail()), user);
    }

    @Test
    void createUser() {
        AdminCreateUserTo adminCreateUserTo = new AdminCreateUserTo();
        adminCreateUserTo.setName("Name");
        adminCreateUserTo.setSurname("Surname");
        adminCreateUserTo.setEmail("email@gmail.com");
        adminCreateUserTo.setPassword("password");
        adminCreateUserTo.setRoles(singleton(UserRoles.ROLE_USER));
        User user = service.createUser(adminCreateUserTo);

        assertMatch(getCache(user.getId()), user);
        assertMatch(getCache(user.getEmail()), user);
    }

    @Test
    void getByEmail() {
        assertThat(getCache(DEFAULT_USER_EMAIL)).isNull();
        User user = service.getByEmail(DEFAULT_USER_EMAIL);
        assertMatch(user, getCache(DEFAULT_USER_EMAIL));
    }

    @Test
    void getById() {
        assertThat(getCache(DEFAULT_USER_ID)).isNull();
        User user = service.getById(DEFAULT_USER_ID);
        assertMatch(user, getCache(DEFAULT_USER_ID));
    }

    @Test
    void updateProfile() {
        User user = service.getById(DEFAULT_USER_ID);
        service.getByEmail(DEFAULT_USER_EMAIL);
        assertMatch(user, getCache(DEFAULT_USER_ID));
        assertMatch(user, getCache(DEFAULT_USER_EMAIL));

        UpdateProfileTo updateProfileTo = new UpdateProfileTo();
        updateProfileTo.setName("NewName");
        updateProfileTo.setSurname("NewSurname");
        User updated = service.updateUser(DEFAULT_USER_ID, updateProfileTo);

        assertMatch(updated, getCache(DEFAULT_USER_ID));
        assertMatch(updated, getCache(DEFAULT_USER_EMAIL));
    }

    @Test
    void updateEmail() {
        User user = service.getById(DEFAULT_USER_ID);
        service.getByEmail(DEFAULT_USER_EMAIL);
        assertMatch(user, getCache(DEFAULT_USER_ID));
        assertMatch(user, getCache(DEFAULT_USER_EMAIL));

        final String newEmail = "newemail@gmail.com";
        UpdateEmailTo updateEmailTo = new UpdateEmailTo();
        updateEmailTo.setEmail(newEmail);
        User updated = service.updateUser(DEFAULT_USER_ID, updateEmailTo);
        assertMatch(updated, getCache(DEFAULT_USER_ID));
        assertMatch(updated, getCache(newEmail));
        assertThat(getCache(DEFAULT_USER_EMAIL)).isNull();
    }

    @Test
    void updatePassword() {
        User user = service.getById(DEFAULT_USER_ID);
        service.getByEmail(DEFAULT_USER_EMAIL);
        assertMatch(user, getCache(DEFAULT_USER_ID));
        assertMatch(user, getCache(DEFAULT_USER_EMAIL));

        UpdatePasswordTo updatePasswordTo = new UpdatePasswordTo();
        updatePasswordTo.setNewPassword("password");
        User updated = service.updateUser(DEFAULT_USER_ID, updatePasswordTo);
        assertMatch(updated, getCache(DEFAULT_USER_ID));
        assertMatch(updated, getCache(DEFAULT_USER_EMAIL));
    }

    @Test
    void updateUser() {
        User user = service.getById(DEFAULT_USER_ID);
        service.getByEmail(DEFAULT_USER_EMAIL);
        assertMatch(user, getCache(DEFAULT_USER_ID));
        assertMatch(user, getCache(DEFAULT_USER_EMAIL));

        final String newEmail = "newemail@gmail.com";
        AdminUpdateUserTo adminUpdateUserTo = new AdminUpdateUserTo();
        adminUpdateUserTo.setEmail(newEmail);
        adminUpdateUserTo.setName("NewName");
        adminUpdateUserTo.setSurname("NewSurname");
        adminUpdateUserTo.setRoles(singleton(UserRoles.ROLE_USER));
        User updated = service.updateUser(DEFAULT_USER_ID, adminUpdateUserTo);
        assertMatch(updated, getCache(DEFAULT_USER_ID));
        assertMatch(updated, getCache(newEmail));
        assertThat(getCache(DEFAULT_USER_EMAIL)).isNull();
    }

    @Test
    void deleteUserById() {
        User user = service.getById(DEFAULT_USER_ID);
        service.getByEmail(DEFAULT_USER_EMAIL);
        assertMatch(user, getCache(DEFAULT_USER_ID));

        service.deleteUserById(DEFAULT_USER_ID);
        assertThat(getCache(DEFAULT_USER_ID)).isNull();
        assertThat(getCache(DEFAULT_USER_EMAIL)).isNull();
    }

    private User getCache(int id) {
        return getCache("user", id);
    }

    private User getCache(String email) {
        return getCache("user_email", email);
    }

    private User getCache(String name, Object key) {
        return requireNonNull(cacheManager.getCache(name)).get(key, User.class);
    }
}
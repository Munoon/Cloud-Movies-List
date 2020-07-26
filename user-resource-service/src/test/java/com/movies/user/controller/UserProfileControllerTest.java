package com.movies.user.controller;

import com.movies.common.error.ErrorInfoField;
import com.movies.common.error.ErrorType;
import com.movies.common.user.User;
import com.movies.common.user.UserMapper;
import com.movies.user.user.UserService;
import com.movies.user.user.to.UpdateEmailTo;
import com.movies.user.user.to.UpdatePasswordTo;
import com.movies.user.user.to.UpdateProfileTo;
import com.movies.user.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.movies.user.user.UserTestData.*;
import static com.movies.user.util.TestUtils.defaultUser;
import static com.movies.user.util.TestUtils.errorType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserProfileControllerTest extends AbstractWebTest {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void updateProfile() throws Exception {
        UpdateProfileTo updateProfileTo = new UpdateProfileTo();
        updateProfileTo.setName("NewName");
        updateProfileTo.setSurname("NewSurname");

        User expected = new User(DEFAULT_USER);
        expected.setName("NewName");
        expected.setSurname("NewSurname");

        mockMvc.perform(post("/profile/update")
                .with(defaultUser())
                .content(JsonUtil.writeValue(updateProfileTo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(contentJson(UserMapper.INSTANCE.asTo(expected)));

        User actual = userService.getById(DEFAULT_USER_ID);
        assertMatch(actual, expected);
    }

    @Test
    void updateEmail() throws Exception {
        UpdateEmailTo updateEmailTo = new UpdateEmailTo();
        updateEmailTo.setEmail("newEmail@example.com");

        User expected = new User(DEFAULT_USER);
        expected.setEmail("newemail@example.com");

        mockMvc.perform(post("/profile/update/email")
                .with(defaultUser())
                .content(JsonUtil.writeValue(updateEmailTo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(contentJson(UserMapper.INSTANCE.asTo(expected)));

        User actual = userService.getById(DEFAULT_USER_ID);
        assertMatch(actual, expected);
    }

    @Test
    void updatePassword() throws Exception {
        UpdatePasswordTo updatePasswordTo = new UpdatePasswordTo();
        updatePasswordTo.setOldPassword("pass");
        updatePasswordTo.setNewPassword("newPassword");

        mockMvc.perform(post("/profile/update/password")
                .with(defaultUser())
                .content(JsonUtil.writeValue(updatePasswordTo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(contentJson(UserMapper.INSTANCE.asTo(DEFAULT_USER)));

        User actual = userService.getById(DEFAULT_USER_ID);
        assertThat(passwordEncoder.matches("newPassword", actual.getPassword())).isTrue();
    }

    @Test
    void updatePasswordNotValid() throws Exception {
        UpdatePasswordTo updatePasswordTo = new UpdatePasswordTo();
        updatePasswordTo.setOldPassword("notValidPassword");
        updatePasswordTo.setNewPassword("newPassword");

        mockMvc.perform(post("/profile/update/password")
                .with(defaultUser())
                .content(JsonUtil.writeValue(updatePasswordTo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR, ErrorInfoField.class));

        User actual = userService.getById(DEFAULT_USER_ID);
        assertThat(passwordEncoder.matches("pass", actual.getPassword())).isTrue();
    }
}
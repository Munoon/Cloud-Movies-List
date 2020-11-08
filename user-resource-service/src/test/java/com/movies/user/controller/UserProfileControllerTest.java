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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static com.movies.user.user.UserTestData.*;
import static com.movies.user.util.TestUtils.errorType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserProfileControllerTest extends AbstractWebTest {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void getProfile() throws Exception {
        mockMvc.perform(get("/profile")
                .with(defaultUser())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(contentJson(DEFAULT_USER));
    }

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
        updatePasswordTo.setOldPassword("password");
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
        assertThat(passwordEncoder.matches("password", actual.getPassword())).isTrue();
    }

    @Test
    void deleteAccount() throws Exception {
        PageRequest request = PageRequest.of(0, 10);
        assertThat(userService.findAll(request)).hasSize(1);

        mockMvc.perform(delete("/profile")
                .with(defaultUser())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(Collections.singletonMap("password", "password"))))
                .andExpect(status().isOk());

        assertThat(userService.findAll(request)).hasSize(0);
    }

    @Test
    void deleteAccountInCorrectPassword() throws Exception {
        PageRequest request = PageRequest.of(0, 10);
        assertThat(userService.findAll(request)).hasSize(1);

        mockMvc.perform(delete("/profile")
                .with(defaultUser())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(Collections.singletonMap("password", "inCorrectPassword"))))
                .andExpect(status().isUnprocessableEntity());

        assertThat(userService.findAll(request)).hasSize(1);
    }
}
package com.movies.user.controller;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import com.movies.user.user.UserRepository;
import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.util.JsonUtil;
import com.movies.user.util.mapper.LocalUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.movies.user.user.UserTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserInfoControllerTest extends AbstractWebTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void register() throws Exception {
        RegisterUserTo registerUserTo = new RegisterUserTo();
        registerUserTo.setName("New");
        registerUserTo.setSurname("User");
        registerUserTo.setEmail("test@example.com");
        registerUserTo.setPassword("examplePassword");

        mockMvc.perform(post("/register")
                .content(JsonUtil.writeValue(registerUserTo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User expected = new User(null, "New", "User", "test@example.com", null, null, Collections.singleton(UserRoles.ROLE_USER));
        List<User> allUsers = userRepository.findAll()
                .stream()
                .map(LocalUserMapper.INSTANCE::asUser)
                .collect(Collectors.toList());
        assertMatchIgnoreId(allUsers, DEFAULT_USER, expected);
    }

    @Test
    void getUserForLogin() throws Exception {
        MvcResult result = mockMvc.perform(get("/login/" + DEFAULT_USER_EMAIL))
                .andExpect(status().isOk())
                .andReturn();

        User user = JsonUtil.readFromJson(result, User.class);
        assertMatch(user, DEFAULT_USER);
        assertThat(DEFAULT_USER.getPassword()).isEqualTo(user.getPassword());
    }
}
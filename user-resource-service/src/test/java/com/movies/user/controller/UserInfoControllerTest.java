package com.movies.user.controller;

import com.movies.user.TestUtils;
import com.movies.user.user.User;
import com.movies.user.user.UserRepository;
import com.movies.user.user.UserRoles;
import com.movies.user.user.to.RegisterUserTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Collections;

import static com.movies.user.user.UserTestData.DEFAULT_USER;
import static com.movies.user.user.UserTestData.assertMatchIgnoreId;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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
                .content(TestUtils.writeValue(registerUserTo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User expected = new User(null, "New", "User", "test@example.com", null, null, Collections.singleton(UserRoles.ROLE_USER));
        assertMatchIgnoreId(userRepository.findAll(), DEFAULT_USER, expected);
    }
}
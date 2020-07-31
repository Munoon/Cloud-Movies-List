package com.movies.user.controller;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import com.movies.common.user.UserTo;
import com.movies.user.user.UserService;
import com.movies.user.user.to.AdminCreateUserTo;
import com.movies.user.user.to.AdminUpdateUserTo;
import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.util.JsonUtil;
import com.movies.user.util.mapper.LocalUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.movies.user.user.UserTestData.*;
import static com.movies.user.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminUsersControllerTest extends AbstractWebTest {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void noAccess() throws Exception {
        RegisterUserTo registerUserTo = new RegisterUserTo();
        registerUserTo.setEmail("test@example.com");
        registerUserTo.setName("Test");
        registerUserTo.setSurname("Surname");
        registerUserTo.setPassword("password");
        User user = userService.createUser(registerUserTo);

        mockMvc.perform(get("/admin/list")
                .with(customUser(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void usersList() throws Exception {
        RegisterUserTo registerUserTo = new RegisterUserTo();
        registerUserTo.setEmail("test@example.com");
        registerUserTo.setName("Test");
        registerUserTo.setSurname("Surname");
        registerUserTo.setPassword("password");
        User user = userService.createUser(registerUserTo);

        mockMvc.perform(get("/admin/list")
                .with(defaultUser())
                .param("page", "0")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.first.href", is("http://localhost/admin/list?page=0&size=1&sort=registered,desc")))
                .andExpect(jsonPath("_links.self.href", is("http://localhost/admin/list?page=0&size=1&sort=registered,desc")))
                .andExpect(jsonPath("_links.next.href", is("http://localhost/admin/list?page=1&size=1&sort=registered,desc")))
                .andExpect(jsonPath("_links.last.href", is("http://localhost/admin/list?page=1&size=1&sort=registered,desc")))
                .andExpect(jsonPath("page.size", is(1)))
                .andExpect(jsonPath("page.totalElements", is(2)))
                .andExpect(jsonPath("page.totalPages", is(2)))
                .andExpect(jsonPath("page.number", is(0)))
                .andExpect(contentJsonHateos(user));

        mockMvc.perform(get("/admin/list")
                .with(defaultUser())
                .param("page", "1")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.first.href", is("http://localhost/admin/list?page=0&size=1&sort=registered,desc")))
                .andExpect(jsonPath("_links.prev.href", is("http://localhost/admin/list?page=0&size=1&sort=registered,desc")))
                .andExpect(jsonPath("_links.self.href", is("http://localhost/admin/list?page=1&size=1&sort=registered,desc")))
                .andExpect(jsonPath("_links.last.href", is("http://localhost/admin/list?page=1&size=1&sort=registered,desc")))
                .andExpect(jsonPath("page.size", is(1)))
                .andExpect(jsonPath("page.totalElements", is(2)))
                .andExpect(jsonPath("page.totalPages", is(2)))
                .andExpect(jsonPath("page.number", is(1)))
                .andExpect(contentJsonHateos(DEFAULT_USER));
    }

    @Test
    void usersListSizeNotValid() throws Exception {
        mockMvc.perform(get("/admin/list")
                .with(defaultUser())
                .param("page", "0")
                .param("size", "9999"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateUser() throws Exception {
        AdminUpdateUserTo adminUpdateUserTo = new AdminUpdateUserTo();
        adminUpdateUserTo.setEmail("example@example.com");
        adminUpdateUserTo.setName("NewName");
        adminUpdateUserTo.setSurname("NewSurname");
        adminUpdateUserTo.setRoles(Collections.singleton(UserRoles.ROLE_USER));

        mockMvc.perform(put("/admin/" + DEFAULT_USER_ID)
                .with(defaultUser())
                .content(JsonUtil.writeValue(adminUpdateUserTo))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        User actual = userService.getById(DEFAULT_USER_ID);
        User expected = new User(DEFAULT_USER);
        expected.setEmail("example@example.com");
        expected.setName("NewName");
        expected.setSurname("NewSurname");
        expected.setRoles(Collections.singleton(UserRoles.ROLE_USER));

        assertMatch(actual, expected);
    }

    @Test
    void updateUserNotFound() throws Exception {
        AdminUpdateUserTo adminUpdateUserTo = new AdminUpdateUserTo();
        adminUpdateUserTo.setEmail("example@example.com");
        adminUpdateUserTo.setName("NewName");
        adminUpdateUserTo.setSurname("NewSurname");
        adminUpdateUserTo.setRoles(Collections.singleton(UserRoles.ROLE_USER));

        mockMvc.perform(put("/admin/101")
                .with(defaultUser())
                .content(JsonUtil.writeValue(adminUpdateUserTo))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById() throws Exception {
        mockMvc.perform(get("/admin/" + DEFAULT_USER_ID)
                .with(defaultUser()))
                .andExpect(status().isOk())
                .andExpect(contentJson(DEFAULT_USER));
    }

    @Test
    void deleteUser() throws Exception {
        Page<User> users = userService.findAll(PageRequest.of(0, 10));
        assertThat(users).hasSize(1);

        mockMvc.perform(delete("/admin/" + DEFAULT_USER_ID)
                .with(defaultUser()))
                .andExpect(status().isOk());

        Page<User> newUsers = userService.findAll(PageRequest.of(0, 10));
        assertThat(newUsers).hasSize(0);
    }

    @Test
    void createUser() throws Exception {
        AdminCreateUserTo adminCreateUserTo = new AdminCreateUserTo();
        adminCreateUserTo.setName("New");
        adminCreateUserTo.setSurname("User");
        adminCreateUserTo.setEmail("Test@example.com");
        adminCreateUserTo.setPassword("examplePassword");
        adminCreateUserTo.setRoles(Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));

        MvcResult mvcResult = mockMvc.perform(post("/admin/create")
                .with(defaultUser())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(adminCreateUserTo)))
                .andExpect(status().isOk())
                .andReturn();

        UserTo userTo = JsonUtil.readFromJson(mvcResult, UserTo.class);

        User expected = new User(userTo.getId(), "New", "User", "test@example.com", null, null, Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));
        List<User> allUsers = userService.findAll(PageRequest.of(0, 10)).getContent();
        assertMatch(allUsers, DEFAULT_USER, expected);

        User newUser = userService.getById(userTo.getId());
        assertTrue(passwordEncoder.matches("examplePassword", newUser.getPassword()));
    }
}
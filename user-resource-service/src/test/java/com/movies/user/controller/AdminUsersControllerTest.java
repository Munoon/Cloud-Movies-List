package com.movies.user.controller;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import com.movies.user.user.UserService;
import com.movies.user.user.to.AdminSaveUserTo;
import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Collections;

import static com.movies.user.user.UserTestData.*;
import static com.movies.user.util.TestUtils.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminUsersControllerTest extends AbstractWebTest {
    @Autowired
    private UserService userService;

    @Test
    void noAccess() throws Exception {
        RegisterUserTo registerUserTo = new RegisterUserTo();
        registerUserTo.setEmail("test@example.com");
        registerUserTo.setName("Test");
        registerUserTo.setSurname("Surname");
        registerUserTo.setPassword("password");
        User user = userService.registerUser(registerUserTo);

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
        User user = userService.registerUser(registerUserTo);

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
        AdminSaveUserTo adminSaveUserTo = new AdminSaveUserTo();
        adminSaveUserTo.setEmail("example@example.com");
        adminSaveUserTo.setName("NewName");
        adminSaveUserTo.setSurname("NewSurname");
        adminSaveUserTo.setRoles(Collections.singleton(UserRoles.ROLE_USER));

        mockMvc.perform(put("/admin/" + DEFAULT_USER_ID)
                .with(defaultUser())
                .content(JsonUtil.writeValue(adminSaveUserTo))
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
        AdminSaveUserTo adminSaveUserTo = new AdminSaveUserTo();
        adminSaveUserTo.setEmail("example@example.com");
        adminSaveUserTo.setName("NewName");
        adminSaveUserTo.setSurname("NewSurname");
        adminSaveUserTo.setRoles(Collections.singleton(UserRoles.ROLE_USER));

        mockMvc.perform(put("/admin/101")
                .with(defaultUser())
                .content(JsonUtil.writeValue(adminSaveUserTo))
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
}
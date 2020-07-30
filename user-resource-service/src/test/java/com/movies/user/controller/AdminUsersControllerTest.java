package com.movies.user.controller;

import com.movies.common.user.User;
import com.movies.user.user.UserService;
import com.movies.user.user.to.RegisterUserTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.movies.user.user.UserTestData.DEFAULT_USER;
import static com.movies.user.user.UserTestData.contentJsonHateos;
import static com.movies.user.util.TestUtils.customUser;
import static com.movies.user.util.TestUtils.defaultUser;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .andExpect(jsonPath("_links.first.href", is("http://localhost/admin/list?page=0&size=1")))
                .andExpect(jsonPath("_links.self.href", is("http://localhost/admin/list?page=0&size=1")))
                .andExpect(jsonPath("_links.next.href", is("http://localhost/admin/list?page=1&size=1")))
                .andExpect(jsonPath("_links.last.href", is("http://localhost/admin/list?page=1&size=1")))
                .andExpect(jsonPath("page.size", is(1)))
                .andExpect(jsonPath("page.totalElements", is(2)))
                .andExpect(jsonPath("page.totalPages", is(2)))
                .andExpect(jsonPath("page.number", is(0)))
                .andExpect(contentJsonHateos(DEFAULT_USER));

        mockMvc.perform(get("/admin/list")
                .with(defaultUser())
                .param("page", "1")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.first.href", is("http://localhost/admin/list?page=0&size=1")))
                .andExpect(jsonPath("_links.prev.href", is("http://localhost/admin/list?page=0&size=1")))
                .andExpect(jsonPath("_links.self.href", is("http://localhost/admin/list?page=1&size=1")))
                .andExpect(jsonPath("_links.last.href", is("http://localhost/admin/list?page=1&size=1")))
                .andExpect(jsonPath("page.size", is(1)))
                .andExpect(jsonPath("page.totalElements", is(2)))
                .andExpect(jsonPath("page.totalPages", is(2)))
                .andExpect(jsonPath("page.number", is(1)))
                .andExpect(contentJsonHateos(user));
    }

    @Test
    void usersListSizeNotValid() throws Exception {
        mockMvc.perform(get("/admin/list")
                .with(defaultUser())
                .param("page", "0")
                .param("size", "9999"))
                .andExpect(status().isUnprocessableEntity());
    }
}
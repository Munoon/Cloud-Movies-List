package com.movies.gateway.filter;

import com.movies.gateway.AbstractTest;
import com.movies.gateway.utils.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static com.movies.gateway.utils.TestUtil.authenticate;
import static com.movies.gateway.utils.TestUtil.checkIfAnonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LogoutUserFilterTest extends AbstractTest {
    @Test
    void logoutAfterUpdatingProfileEmail() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("email", "newEmail@gmail.com");

        mockMvc.perform(post("/users/profile/update/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(body))
                .with(authenticate()))
                .andExpect(status().isOk())
                .andExpect(checkIfAnonymous());
    }

    @Test
    void logoutAfterUpdatingProfilePassword() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("oldPassword", "pass");
        body.put("newPassword", "newPassword");

        mockMvc.perform(post("/users/profile/update/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(body))
                .with(authenticate()))
                .andExpect(status().isOk())
                .andExpect(checkIfAnonymous());
    }

    @Test
    void logoutAfterDeletingProfile() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("password", "pass");

        mockMvc.perform(delete("/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(body))
                .with(authenticate()))
                .andExpect(status().isOk())
                .andExpect(checkIfAnonymous());
    }
}
package com.movies.gateway.filter;

import com.movies.common.user.UserRoles;
import com.movies.common.user.UserTo;
import com.movies.gateway.AbstractTest;
import com.movies.gateway.utils.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.movies.gateway.utils.TestUtil.authenticate;
import static com.movies.gateway.utils.UserToValidation.userToResponse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LogoutUserFilterTest extends AbstractTest {
    @Test
    void logoutAfterUpdatingProfileEmail() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("email", "newEmail@gmail.com");
        UserTo expected = new UserTo(100, "Nikita", "Ivchenko", "newemail@gmail.com", Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));

        mockMvc.perform(post("/users/profile/update/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(body))
                .with(csrf())
                .with(authenticate()))
                .andExpect(status().isOk())
                .andExpect(unauthenticated())
                .andExpect(userToResponse(expected));
    }

    @Test
    void logoutAfterUpdatingProfilePassword() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("oldPassword", "pass");
        body.put("newPassword", "newPassword");
        UserTo expected = new UserTo(100, "Nikita", "Ivchenko", "munoongg@gmail.com", Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));

        mockMvc.perform(post("/users/profile/update/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(body))
                .with(csrf())
                .with(authenticate()))
                .andExpect(status().isOk())
                .andExpect(unauthenticated())
                .andExpect(userToResponse(expected));
    }

    @Test
    void logoutAfterDeletingProfile() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("password", "pass");

        mockMvc.perform(delete("/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(body))
                .with(csrf())
                .with(authenticate()))
                .andExpect(status().isOk())
                .andExpect(unauthenticated());
    }
}
package com.movies.gateway.filter;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.UserRoles;
import com.movies.common.user.UserTo;
import com.movies.gateway.AbstractTest;
import com.movies.gateway.utils.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.movies.gateway.utils.TestUtil.authenticate;
import static com.movies.gateway.utils.UserToValidation.userToResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateUserDetailsFilterTest extends AbstractTest {
    @Test
    public void updateUserDetailsTest() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("name", "newName");
        body.put("surname", "newSurname");
        UserTo expected = new UserTo(100, "newName", "newSurname", "munoongg@gmail.com", Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));

        mockMvc.perform(post("/users/profile/update")
                .content(JsonUtil.writeValue(body))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(authenticate()))
                .andExpect(authenticated().withAuthenticationPrincipal(new AuthorizedUser(expected)))
                .andExpect(status().isOk())
                .andExpect(userToResponse(expected));
    }
}
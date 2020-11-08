package com.movies.auth.config;

import com.movies.auth.AbstractWebTest;
import com.movies.auth.authentications.UserAuthenticationEntity;
import com.movies.auth.authentications.UserAuthenticationService;
import com.movies.auth.user.UserTestData;
import com.movies.common.AuthorizedUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpSession;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserAuthenticationValidationFilterTest extends AbstractWebTest {
    @Autowired
    private UserAuthenticationService service;

    @Test
    void testAuthenticationValid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        HttpSession session = request.getSession(true);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new AuthorizedUser(UserTestData.DEFAULT_USER),
                UserTestData.DEFAULT_USER.getPassword(),
                UserTestData.DEFAULT_USER.getRoles()
        );
        token.setDetails(new WebAuthenticationDetails(request));

        service.create(new UserAuthenticationEntity(session.getId()));

        mockMvc.perform(get("/login")
                .with(authentication(token)))
                .andExpect(status().isForbidden())
                .andExpect(authenticated());
    }

    @Test
    void testAuthenticationNotValid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.getSession(true);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new AuthorizedUser(UserTestData.DEFAULT_USER),
                UserTestData.DEFAULT_USER.getPassword(),
                UserTestData.DEFAULT_USER.getRoles()
        );
        token.setDetails(new WebAuthenticationDetails(request));

        mockMvc.perform(get("/login")
                .with(authentication(token)))
                .andExpect(status().isOk())
                .andExpect(unauthenticated());
    }
}
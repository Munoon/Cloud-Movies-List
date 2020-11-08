package com.movies.auth.config;

import com.movies.auth.AbstractTest;
import com.movies.auth.authentications.UserAuthenticationEntity;
import com.movies.auth.authentications.UserAuthenticationRepository;
import com.movies.auth.user.UserTestData;
import com.movies.common.AuthorizedUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomAuthenticationSuccessEventListenerTest extends AbstractTest {
    @Autowired
    private AuthenticationEventPublisher eventPublisher;

    @Autowired
    private UserAuthenticationRepository repository;

    @Test
    void testAuthenticationCreating() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession(true);
        request.setRemoteAddr("127.0.0.1");
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new AuthorizedUser(UserTestData.DEFAULT_USER),
                "{noop}password",
                UserTestData.DEFAULT_USER.getRoles()
        );
        token.setDetails(details);

        eventPublisher.publishAuthenticationSuccess(token);

        List<UserAuthenticationEntity> data = repository.findAll();
        assertThat(data).hasSize(1);
        assertThat(data.get(0).getSessionId()).isEqualTo(session.getId());
    }
}
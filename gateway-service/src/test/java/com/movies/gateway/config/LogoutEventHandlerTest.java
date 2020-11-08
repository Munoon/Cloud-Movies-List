package com.movies.gateway.config;

import com.movies.gateway.AbstractTest;
import com.movies.gateway.authentication.UserAuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class LogoutEventHandlerTest extends AbstractTest {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @MockBean
    private UserAuthenticationService service;

    @Test
    void logoutTest() {
        final String jwtToken = "TestToken";
        TestingAuthenticationToken token = new TestingAuthenticationToken(null, null);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, jwtToken);
        request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, "bearer");
        request.getSession(true);

        token.setDetails(new OAuth2AuthenticationDetails(request));
        eventPublisher.publishEvent(new LogoutSuccessEvent(token));

        verify(service, times(1)).logoutByToken(jwtToken);
    }
}
package com.movies.gateway.utils;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

public class TestUtil {
    public static final User DEFAULT_USER = new User(100, "Nikita", "Ivchenko", "munoongg@gmail.com", "{noop}pass", LocalDateTime.now(), Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));
    public static final OAuth2Request DEFAULT_OAUTH2_REQUEST = new OAuth2Request(Collections.emptyMap(), "testClient", Collections.emptyList(), true, Collections.emptySet(), Collections.emptySet(), null, Collections.emptySet(), Collections.emptyMap());

    public static RequestPostProcessor authenticate() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new AuthorizedUser(DEFAULT_USER),
                "N/A",
                DEFAULT_USER.getRoles()
        );
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(DEFAULT_OAUTH2_REQUEST, token);
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, "DEFAULT_USER");
        mockHttpServletRequest.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, "bearer");
        mockHttpServletRequest.getSession(true);
        oAuth2Authentication.setDetails(new OAuth2AuthenticationDetails(mockHttpServletRequest));

        MockHttpSession mockHttpSession = new MockHttpSession();
        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken("DEFAULT_USER");
        mockHttpSession.setAttribute("scopedTarget.oauth2ClientContext", new DefaultOAuth2ClientContext(accessToken));
        mockHttpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContextImpl(oAuth2Authentication));

        SecurityContextHolder.getContext().setAuthentication(oAuth2Authentication);

        return request -> {
            request.setSession(mockHttpSession);
            request.setUserPrincipal(oAuth2Authentication);
            return request;
        };
    }
}

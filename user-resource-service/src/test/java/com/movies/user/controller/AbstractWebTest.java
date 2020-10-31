package com.movies.user.controller;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.User;
import com.movies.user.AbstractTest;
import com.movies.user.user.UserTestData;
import com.movies.user.util.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class AbstractWebTest extends AbstractTest {
    @Autowired
    private WebApplicationContext context;

    @MockBean
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;

    protected MockMvc mockMvc;

    @PostConstruct
    void setupMockMvc() {
        mockMvc = getNewMockMvc();
    }

    protected OAuth2AccessToken getAccessToken(User user, OAuth2Request request, String token) {
        var authUser = new AuthorizedUser(new User(user));
        var authToken = new UsernamePasswordAuthenticationToken(authUser, "N/A", authUser.getAuthorities());
        var authentication = new OAuth2Authentication(request, authToken);

        var accessToken = new DefaultOAuth2AccessToken(token);
        when(tokenStore.readAccessToken(accessToken.getValue())).thenReturn(accessToken);
        when(tokenStore.readAuthentication(accessToken)).thenReturn(authentication);
        return accessToken;
    }

    protected OAuth2AccessToken getAccessToken(User user, OAuth2Request request) {
        return getAccessToken(user, request, UUID.randomUUID().toString());
    }

    protected RequestPostProcessor customUser(User user) {
        OAuth2AccessToken accessToken = getAccessToken(user, TestUtils.DEFAULT_OAUTH_REQUEST);
        String token = String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, accessToken.getValue());
        return req -> {
            req.addHeader("Authorization", token);
            return req;
        };
    }

    protected RequestPostProcessor defaultUser() {
        return customUser(UserTestData.DEFAULT_USER);
    }

    protected MockMvc getNewMockMvc() {
        return MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
}

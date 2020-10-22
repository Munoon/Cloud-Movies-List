package com.movies.user.util;

import com.movies.common.AuthorizedUser;
import com.movies.common.error.ErrorInfo;
import com.movies.common.error.ErrorType;
import com.movies.common.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Collections;

import static com.movies.user.user.UserTestData.DEFAULT_USER;
import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {
    public static final String DEFAULT_CLIENT_ID = "testClient";
    public static final OAuth2Request DEFAULT_OAUTH_REQUEST = new OAuth2Request(
            Collections.singletonMap("client_id", DEFAULT_CLIENT_ID),
            DEFAULT_CLIENT_ID, Collections.emptySet(),
            true, Collections.singleton("user_info"),
            Collections.emptySet(), "http://localhost:8080",
            Collections.emptySet(), Collections.emptyMap());

    public static void addAuthentication(OAuth2Request oAuth2Request, User user) {
        OAuth2Authentication authentication = new OAuth2Authentication(
                oAuth2Request,
                new UsernamePasswordAuthenticationToken(new AuthorizedUser(user), null, user.getRoles())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static RequestPostProcessor customUserAndClient(OAuth2Request oAuth2Request, User user) {
        addAuthentication(oAuth2Request, user);
        return request -> request;
    }


    public static RequestPostProcessor customUser(User user) {
        return customUserAndClient(DEFAULT_OAUTH_REQUEST, user);
    }

    public static RequestPostProcessor defaultUser() {
        return customUser(DEFAULT_USER);
    }

    public static ResultMatcher errorType(ErrorType type, Class<? extends ErrorInfo> responseType) {
        return result -> assertThat(JsonUtil.readFromJson(result, responseType).getType()).isEqualTo(type);
    }
}

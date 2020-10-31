package com.movies.user.util;

import com.movies.common.error.ErrorInfo;
import com.movies.common.error.ErrorType;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {
    public static final String DEFAULT_CLIENT_ID = "testClient";
    public static final OAuth2Request DEFAULT_OAUTH_REQUEST = new OAuth2Request(
            Collections.singletonMap("client_id", DEFAULT_CLIENT_ID),
            DEFAULT_CLIENT_ID, Collections.emptySet(),
            true, Collections.singleton("user_info"),
            Collections.emptySet(), "http://localhost:8080",
            Collections.emptySet(), Collections.emptyMap());

    public static ResultMatcher errorType(ErrorType type, Class<? extends ErrorInfo> responseType) {
        return result -> assertThat(JsonUtil.readFromJson(result, responseType).getType()).isEqualTo(type);
    }
}

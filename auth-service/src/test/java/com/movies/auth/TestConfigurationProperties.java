package com.movies.auth;

public class TestConfigurationProperties {
    public static final String OAUTH_CLIENT_ID = "testClient";
    public static final String OAUTH_CLIENT_SECRET = "{noop}password";
    public static final String OAUTH_CLIENT_REDIRECT_URI = "http://localhost:8080/login,http://10.0.114.148:8080/login";
    public static final String OAUTH_CLIENT_AUTHORIZED_GRANT_TYPES = "authorization_code,refresh_token";
    public static final String OAUTH_CLIENT_SCOPES = "user_info";
    public static final boolean OAUTH_CLIENT_AUTO_APPROVE = true;
    public static final int OAUTH_CLIENT_ACCESS_TOKEN_VALIDITY_SECONDS = 30;
    public static final int OAUTH_CLIENT_REFRESH_TOKEN_VALIDITY_SECONDS = 1800;
}

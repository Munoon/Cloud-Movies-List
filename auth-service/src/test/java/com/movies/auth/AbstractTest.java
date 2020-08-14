package com.movies.auth;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = {
        "oauth.client.id=" + TestConfigurationProperties.OAUTH_CLIENT_ID,
        "oauth.client.secret=" + TestConfigurationProperties.OAUTH_CLIENT_SECRET,
        "oauth.client.redirectUri=" + TestConfigurationProperties.OAUTH_CLIENT_REDIRECT_URI,
        "oauth.client.authorizedGrantTypes=" + TestConfigurationProperties.OAUTH_CLIENT_AUTHORIZED_GRANT_TYPES,
        "oauth.client.scopes=" + TestConfigurationProperties.OAUTH_CLIENT_SCOPES,
        "oauth.client.autoApprove=" + TestConfigurationProperties.OAUTH_CLIENT_AUTO_APPROVE,
        "oauth.client.accessTokenValiditySeconds=" + TestConfigurationProperties.OAUTH_CLIENT_ACCESS_TOKEN_VALIDITY_SECONDS,
        "oauth.client.refreshTokenValiditySeconds=" + TestConfigurationProperties.OAUTH_CLIENT_REFRESH_TOKEN_VALIDITY_SECONDS
})
public abstract class AbstractTest {

}

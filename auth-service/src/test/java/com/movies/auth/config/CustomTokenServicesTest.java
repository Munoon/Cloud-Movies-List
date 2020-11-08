package com.movies.auth.config;

import com.movies.auth.authentications.UserAuthenticationService;
import com.movies.auth.authentications.jwt.UserAuthenticationJwtEntity;
import com.movies.auth.user.UserTestData;
import com.movies.common.AuthorizedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CustomTokenServicesTest {
    private CustomTokenServices customTokenServices;
    private UserAuthenticationService service = Mockito.mock(UserAuthenticationService.class);
    private TokenStore tokenStore = Mockito.mock(TokenStore.class);

    @BeforeEach
    void setup() throws Exception {
        customTokenServices = new CustomTokenServices(service);
        customTokenServices.setTokenStore(tokenStore);
        customTokenServices.afterPropertiesSet();
    }

    @Test
    void createAccessToken() {
        when(tokenStore.readAccessToken(anyString())).thenReturn(new DefaultOAuth2AccessToken("FOO"));
        when(service.create((UserAuthenticationJwtEntity) any())).thenReturn(new UserAuthenticationJwtEntity());

        OAuth2Authentication authentication = new OAuth2Authentication(
                new OAuth2Request(emptyMap(), "", anyCollection(), true, emptySet(), emptySet(), "", emptySet(), emptyMap()),
                new UsernamePasswordAuthenticationToken(
                        new AuthorizedUser(UserTestData.DEFAULT_USER),
                        "N/A",
                        UserTestData.DEFAULT_USER.getRoles()
                )
        );
        customTokenServices.createAccessToken(authentication);

        verify(service, times(1)).create((UserAuthenticationJwtEntity) any());
    }
}
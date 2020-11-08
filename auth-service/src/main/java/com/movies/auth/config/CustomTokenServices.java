package com.movies.auth.config;

import com.movies.auth.authentications.UserAuthenticationService;
import com.movies.auth.authentications.UserAuthenticationUtil;
import com.movies.auth.authentications.jwt.UserAuthenticationJwtEntity;
import com.movies.auth.authentications.UserAuthenticationEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Slf4j
@AllArgsConstructor
public class CustomTokenServices extends DefaultTokenServices {
    private final UserAuthenticationService service;

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        String sessionId = UserAuthenticationUtil.getSessionId(authentication);
        OAuth2AccessToken accessToken = super.createAccessToken(authentication);
        saveToken(accessToken, sessionId);
        return accessToken;
    }

    private void saveToken(OAuth2AccessToken accessToken, String sessionId) {
        UserAuthenticationJwtEntity entity = new UserAuthenticationJwtEntity();
        entity.setToken(accessToken.getValue());
        entity.setExpiration(accessToken.getExpiration());
        entity.setAuthentication(new UserAuthenticationEntity(sessionId));
        service.create(entity);
        log.info("Saved user authentication JWT token: {}", entity);
    }
}

package com.movies.gateway.utils;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.UserTo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class SecurityUtils {
    public static final String AUTHORIZATION_TOKEN_HEADER_NAME = "Authorization";

    public static void updateUser(UserTo userTo) {
        SecurityContext context = SecurityContextHolder.getContext();
        OAuth2Authentication authentication = (OAuth2Authentication) context.getAuthentication();

        var token = new UsernamePasswordAuthenticationToken(
                new AuthorizedUser(userTo),
                "N/A",
                userTo.getRoles()
        );
        var newAuthentication = new OAuth2Authentication(authentication.getOAuth2Request(), token);
        newAuthentication.setDetails(authentication.getDetails());
        context.setAuthentication(newAuthentication);
    }
}

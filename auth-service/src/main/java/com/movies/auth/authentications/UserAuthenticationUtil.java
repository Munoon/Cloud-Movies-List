package com.movies.auth.authentications;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class UserAuthenticationUtil {
    public static String getSessionId(Authentication authentication) {
        Object details = authentication.getDetails();
        if (authentication instanceof OAuth2Authentication) {
            details = ((OAuth2Authentication) authentication).getUserAuthentication().getDetails();
        }
        return details instanceof WebAuthenticationDetails ? ((WebAuthenticationDetails) details).getSessionId() : null;
    }
}

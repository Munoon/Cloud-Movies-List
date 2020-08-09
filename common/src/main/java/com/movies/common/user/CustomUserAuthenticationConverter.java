package com.movies.common.user;

import com.movies.common.AuthorizedUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.Collections;
import java.util.Map;

public abstract class CustomUserAuthenticationConverter implements UserAuthenticationConverter {
    private static final String USER_ID_KEY = "user";

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        return Collections.singletonMap(USER_ID_KEY, authUser.getId());
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USER_ID_KEY)) {
            int userId = (int) map.get(USER_ID_KEY);
            AuthorizedUser principal = getAuthorizedUser(userId);
            return new UsernamePasswordAuthenticationToken(principal, "N/A", principal.getAuthorities());
        }
        return null;
    }

    public abstract AuthorizedUser getAuthorizedUser(int userId);
}

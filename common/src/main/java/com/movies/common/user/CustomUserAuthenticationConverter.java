package com.movies.common.user;

import com.movies.common.AuthorizedUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.Collections;
import java.util.Map;

public class CustomUserAuthenticationConverter implements UserAuthenticationConverter {
    private static final String USER_MODEL_KEY = "user";

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        Map<String, ?> userToMap = UserMapper.INSTANCE.asMap(authUser.getUserTo());
        return Collections.singletonMap(USER_MODEL_KEY, userToMap);
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USER_MODEL_KEY)) {
            Map<String, Object> userMap = (Map<String, Object>) map.get(USER_MODEL_KEY);
            AuthorizedUser principal = new AuthorizedUser(userMap);
            return new UsernamePasswordAuthenticationToken(principal, "N/A", principal.getAuthorities());
        }
        return null;
    }
}

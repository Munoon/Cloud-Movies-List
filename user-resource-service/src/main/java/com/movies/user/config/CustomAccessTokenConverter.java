package com.movies.user.config;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.User;
import com.movies.user.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.Map;

@AllArgsConstructor
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {
    private UserService userService;

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = super.extractAuthentication(map);

        String email = (String) authentication.getUserAuthentication().getPrincipal();
        User user = userService.getByEmail(email);

        var token = new UsernamePasswordAuthenticationToken(
                new AuthorizedUser(user),
                "N/A",
                authentication.getUserAuthentication().getAuthorities()
        );

        return new OAuth2Authentication(authentication.getOAuth2Request(), token);
    }
}

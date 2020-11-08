package com.movies.gateway.config;

import com.movies.gateway.authentication.UserAuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LogoutEventHandler {
    private UserAuthenticationService service;

    @EventListener(LogoutSuccessEvent.class)
    public void onLogoutSuccess(LogoutSuccessEvent e) {
        if (!(e.getAuthentication().getDetails() instanceof OAuth2AuthenticationDetails)) {
            return;
        }

        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) e.getAuthentication().getDetails();
        String token = oAuth2AuthenticationDetails.getTokenValue();
        try {
            service.logoutByToken(token);
            log.info("Sent logout event to user-resource-service for user {}", e.getAuthentication());
        } catch (Exception ex) {
            log.error("Error invalidating user token", ex);
        }
    }
}

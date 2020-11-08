package com.movies.auth.config;

import com.movies.auth.authentications.UserAuthenticationEntity;
import com.movies.auth.authentications.UserAuthenticationService;
import com.movies.common.AuthorizedUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CustomAuthenticationSuccessEventListener {
    private UserAuthenticationService service;

    @EventListener(AuthenticationSuccessEvent.class)
    public void authenticationSuccessEventHandler(AuthenticationSuccessEvent e) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) e.getAuthentication().getDetails();

        if (e.getAuthentication().getPrincipal() instanceof AuthorizedUser && details.getSessionId() != null) {
            UserAuthenticationEntity entity = new UserAuthenticationEntity();
            entity.setSessionId(details.getSessionId());

            service.create(entity);
            log.info("Created user authentication: {}", entity);
        }
    }
}

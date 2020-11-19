package com.movies.auth.config;

import com.movies.auth.authentications.UserAuthenticationEntity;
import com.movies.auth.authentications.UserAuthenticationService;
import com.movies.common.AuthorizedUser;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEventListener {
    private final UserAuthenticationService service;
    private final Counter successLoginCounter;
    private final Counter unSuccessLoginCounter;

    public AuthenticationEventListener(UserAuthenticationService service, MeterRegistry registry) {
        this.service = service;
        this.successLoginCounter = registry.counter(MetricsConfig.SUCCESS_LOGIN_COUNTER_KEY);
        this.unSuccessLoginCounter = registry.counter(MetricsConfig.UN_SUCCESS_LOGIN_COUNTER_KEY);
    }

    @EventListener(AuthenticationSuccessEvent.class)
    public void authenticationSuccessEventHandler(AuthenticationSuccessEvent e) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) e.getAuthentication().getDetails();

        if (e.getAuthentication().getPrincipal() instanceof AuthorizedUser && details.getSessionId() != null) {
            successLoginCounter.increment();
            UserAuthenticationEntity entity = new UserAuthenticationEntity();
            entity.setSessionId(details.getSessionId());

            service.create(entity);
            log.info("Created user authentication: {}", entity);
        }
    }

    @EventListener(AbstractAuthenticationFailureEvent.class)
    public void authenticationFailureEventHandler() {
        unSuccessLoginCounter.increment();
    }
}

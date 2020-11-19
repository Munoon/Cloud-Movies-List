package com.movies.auth.config;

import com.movies.auth.AbstractTest;
import com.movies.auth.authentications.UserAuthenticationEntity;
import com.movies.auth.authentications.UserAuthenticationRepository;
import com.movies.auth.user.UserTestData;
import com.movies.common.AuthorizedUser;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationEventListenerTest extends AbstractTest {
    @Autowired
    private AuthenticationEventPublisher eventPublisher;

    @Autowired
    private UserAuthenticationRepository repository;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    void testAuthenticationCreating() {
        Counter counter = meterRegistry.get(MetricsConfig.SUCCESS_LOGIN_COUNTER_KEY).counter();
        assertThat(counter).isNotNull();
        double startCount = counter.count();

        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession(true);
        request.setRemoteAddr("127.0.0.1");
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new AuthorizedUser(UserTestData.DEFAULT_USER),
                "{noop}password",
                UserTestData.DEFAULT_USER.getRoles()
        );
        token.setDetails(details);

        eventPublisher.publishAuthenticationSuccess(token);

        List<UserAuthenticationEntity> data = repository.findAll();
        assertThat(data).hasSize(1);
        assertThat(data.get(0).getSessionId()).isEqualTo(session.getId());
        assertThat(counter.count()).isEqualTo(startCount + 1);
    }

    @Test
    void testUnSuccessMetricCounterIncremented() {
        Counter counter = meterRegistry.get(MetricsConfig.UN_SUCCESS_LOGIN_COUNTER_KEY).counter();
        assertThat(counter).isNotNull();
        double startCount = counter.count();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new AuthorizedUser(UserTestData.DEFAULT_USER),
                "{noop}password",
                UserTestData.DEFAULT_USER.getRoles()
        );
        eventPublisher.publishAuthenticationFailure(new BadCredentialsException("test"), token);

        assertThat(counter.count()).isEqualTo(startCount + 1);
    }
}
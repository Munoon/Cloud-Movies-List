package com.movies.user.user;

import com.movies.user.AbstractTest;
import com.movies.user.config.MetricsConfig;
import com.movies.user.user.to.RegisterUserTo;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.movies.user.user.UserTestData.DEFAULT_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

class UserServiceMetricsTest extends AbstractTest {
    @Autowired
    private UserService service;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    void registerUser() {
        Counter counter = meterRegistry.get(MetricsConfig.USERS_REGISTERED_COUNTER_NAME).counter();
        assertThat(counter).isNotNull();
        double startCount = counter.count();

        RegisterUserTo registerUserTo = new RegisterUserTo();
        registerUserTo.setName("Name");
        registerUserTo.setSurname("Surname");
        registerUserTo.setEmail("email@gmail.com");
        registerUserTo.setPassword("password");
        service.createUser(registerUserTo);

        assertThat(counter.count()).isEqualTo(startCount + 1);
    }

    @Test
    void deleteUserById() {
        Counter counter = meterRegistry.get(MetricsConfig.USERS_DELETED_COUNTER_NAME).counter();
        assertThat(counter).isNotNull();
        double startCount = counter.count();

        service.deleteUserById(DEFAULT_USER_ID);

        assertThat(counter.count()).isEqualTo(startCount + 1);
    }

    @Test
    void usersCount() {
        Gauge gauge = meterRegistry.get(MetricsConfig.USERS_COUNT_NAME).gauge();
        assertThat(gauge).isNotNull();
        assertThat(gauge.value()).isEqualTo(1);

        RegisterUserTo registerUserTo = new RegisterUserTo();
        registerUserTo.setName("Name");
        registerUserTo.setSurname("Surname");
        registerUserTo.setEmail("email@gmail.com");
        registerUserTo.setPassword("password");
        service.createUser(registerUserTo);

        assertThat(gauge.value()).isEqualTo(2);
    }
}
package com.movies.user.config;

import com.movies.user.user.UserService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class MetricsConfig {
    public static final String USERS_REGISTERED_COUNTER_NAME = "users.registered";
    public static final String USERS_DELETED_COUNTER_NAME = "users.deleted";
    public static final String USERS_COUNT_NAME = "users.count";

    @Bean
    MeterRegistryCustomizer<MeterRegistry> registeredUsersRegistry() {
        return registry -> registry.config()
                .namingConvention()
                .name(USERS_REGISTERED_COUNTER_NAME, Meter.Type.COUNTER);
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> deletedUsersRegistry() {
        return registry -> registry.config()
                .namingConvention()
                .name(USERS_DELETED_COUNTER_NAME, Meter.Type.COUNTER);
    }

    @Bean
    MeterBinder usersCounterMeterBinder(@Lazy UserService userService) {
        return meterRegistry -> Gauge.builder(USERS_COUNT_NAME, userService, UserService::count)
                .description("Total count of users")
                .register(meterRegistry);
    }
}

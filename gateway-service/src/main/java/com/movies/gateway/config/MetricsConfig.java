package com.movies.gateway.config;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    public static final String USER_LOGOUT_COUNTER_NAME = "account.logout.success";

    @Bean
    MeterRegistryCustomizer<MeterRegistry> registeredUsersRegistry() {
        return registry -> registry.config()
                .namingConvention()
                .name(USER_LOGOUT_COUNTER_NAME, Meter.Type.COUNTER);
    }
}

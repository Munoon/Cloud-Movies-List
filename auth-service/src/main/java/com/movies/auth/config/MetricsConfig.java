package com.movies.auth.config;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    public static final String SUCCESS_LOGIN_COUNTER_KEY = "account.login.success";
    public static final String UN_SUCCESS_LOGIN_COUNTER_KEY = "account.login.un_success";

    @Bean
    MeterRegistryCustomizer<MeterRegistry> registeredUsersRegistry() {
        return registry -> registry.config()
                .namingConvention()
                .name(SUCCESS_LOGIN_COUNTER_KEY, Meter.Type.COUNTER);
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> deletedUsersRegistry() {
        return registry -> registry.config()
                .namingConvention()
                .name(UN_SUCCESS_LOGIN_COUNTER_KEY, Meter.Type.COUNTER);
    }
}

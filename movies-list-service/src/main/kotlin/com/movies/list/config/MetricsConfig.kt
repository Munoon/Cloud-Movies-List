package com.movies.list.config

import com.movies.list.movies.MoviesService
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.binder.MeterBinder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricsConfig {
    @Bean
    fun moviesCountMeterBinder(moviesService: MoviesService) = MeterBinder { meterRegistry ->
        Gauge.builder("movies.count", moviesService, { it.count().toDouble() })
                .description("Total count of movies")
                .register(meterRegistry)
    }
}
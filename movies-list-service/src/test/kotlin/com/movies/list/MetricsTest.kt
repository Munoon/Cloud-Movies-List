package com.movies.list

import com.movies.list.movies.MoviesGenres
import com.movies.list.movies.MoviesService
import com.movies.list.movies.to.CreateMovieTo
import com.neovisionaries.i18n.CountryCode
import io.micrometer.core.instrument.MeterRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

internal class MetricsTest : AbstractTest() {
    @Autowired
    private lateinit var moviesService: MoviesService

    @Autowired
    private lateinit var meterRegistry: MeterRegistry

    @Test
    fun moviesCountMetric() {
        val gauge = meterRegistry.get("movies.count").gauge()
        assertThat(gauge.value()).isEqualTo(0.0)

        moviesService.createMovie(CreateMovieTo("Test Movie", "Original Name", "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"), null)

        assertThat(gauge.value()).isEqualTo(1.0)
    }
}
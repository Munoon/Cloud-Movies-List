package com.movies.list.movies

import com.movies.common.movie.MovieTo
import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.CreateMoviesTo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class MovieMapperTest {
    @Test
    internal fun asMovie() {
        val movie = MovieMapper.INSTANCE.asMovie(CreateMoviesTo("Test Name"))
        assertMatch(movie, Movie(null, "Test Name", movie.registered))
    }

    @Test
    internal fun asMovieTo() {
        val registered = LocalDateTime.now();
        val movieTo = MovieMapper.INSTANCE.asMovieTo(Movie("100", "testName", LocalDateTime.from(registered)))
        assertThat(movieTo).isEqualToComparingFieldByField(MovieTo("100", "testName", LocalDateTime.from(registered)))
    }
}
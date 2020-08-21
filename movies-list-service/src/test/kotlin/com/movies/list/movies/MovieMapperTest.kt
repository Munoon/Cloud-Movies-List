package com.movies.list.movies

import com.movies.common.movie.MovieTo
import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.CreateMoviesTo
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.Binary
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class MovieMapperTest {
    @Test
    internal fun asMovie() {
        val movie = MovieMapper.INSTANCE.asMovie(CreateMoviesTo("Test Name", null))
        assertMatch(movie, Movie(null, "Test Name", null, movie.registered))
    }

    @Test
    internal fun asMovieTo() {
        val registered = LocalDateTime.now();
        val movieTo = MovieMapper.INSTANCE.asMovieTo(Movie("100", "testName", null, LocalDateTime.from(registered)))
        assertThat(movieTo).isEqualToComparingFieldByField(MovieTo("100", "testName", false, LocalDateTime.from(registered)))


        val movieTo2 = MovieMapper.INSTANCE.asMovieTo(Movie("100", "testName", Binary(ByteArray(1)), LocalDateTime.from(registered)))
        assertThat(movieTo2).isEqualToComparingFieldByField(MovieTo("100", "testName", true, LocalDateTime.from(registered)))
    }
}
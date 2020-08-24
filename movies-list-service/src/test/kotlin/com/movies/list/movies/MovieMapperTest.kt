package com.movies.list.movies

import com.movies.common.movie.SmallMovieTo
import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.CreateMoviesTo
import com.movies.list.movies.to.MovieTo
import com.neovisionaries.i18n.CountryCode
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.Binary
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

internal class MovieMapperTest {
    @Test
    internal fun asMovie() {
        val movie = MovieMapper.INSTANCE.asMovie(CreateMoviesTo("Test Name", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "16+", "1:12:23"))
        assertMatch(movie, Movie(null, "Test Name", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), movie.premiere, "16+", "1:12:23", movie.registered))
    }

    @Test
    internal fun asMovieTo() {
        val registered = LocalDateTime.now();
        val movieTo = MovieMapper.INSTANCE.asMovieTo(Movie("100", "testName", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "16+", "1:12", LocalDateTime.from(registered)))
        assertThat(movieTo).isEqualToComparingFieldByField(MovieTo("100", "testName", "Original Name", false, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "16+", "1:12", LocalDateTime.from(registered)))

        val movieTo2 = MovieMapper.INSTANCE.asMovieTo(Movie("100", "testName", "Original Name", Binary(ByteArray(1)), "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "16+", "1:12", LocalDateTime.from(registered)))
        assertThat(movieTo2).isEqualToComparingFieldByField(MovieTo("100", "testName", "Original Name", true, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "16+", "1:12", LocalDateTime.from(registered)))
    }

    @Test
    internal fun asSmallMovie() {
        val registered = LocalDateTime.now();
        val movieTo = MovieMapper.INSTANCE.asSmallMovie(Movie("100", "testName", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "16+", "1:12", LocalDateTime.from(registered)))
        assertThat(movieTo).isEqualToComparingFieldByField(SmallMovieTo("100", "testName", false, LocalDateTime.from(registered)))

        val movieTo2 = MovieMapper.INSTANCE.asSmallMovie(Movie("100", "testName", "Original Name", Binary(ByteArray(1)), "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "16+", "1:12", LocalDateTime.from(registered)))
        assertThat(movieTo2).isEqualToComparingFieldByField(SmallMovieTo("100", "testName", true, LocalDateTime.from(registered)))
    }
}
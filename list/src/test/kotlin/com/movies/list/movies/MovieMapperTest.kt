package com.movies.list.movies

import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.CreateMoviesTo
import org.junit.jupiter.api.Test

internal class MovieMapperTest {
    @Test
    internal fun asMovie() {
        val movie = MovieMapper.INSTANCE.asMovie(CreateMoviesTo("Test Name"))
        assertMatch(movie, Movie(null, "Test Name", movie.registered))
    }
}
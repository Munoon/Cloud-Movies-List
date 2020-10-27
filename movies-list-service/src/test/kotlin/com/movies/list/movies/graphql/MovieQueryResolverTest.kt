package com.movies.list.movies.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import com.graphql.spring.boot.test.GraphQLTestTemplate
import com.movies.list.AbstractTest
import com.movies.list.movies.MoviesGenres
import com.movies.list.movies.MoviesService
import com.movies.list.movies.to.CreateMoviesTo
import com.movies.list.movies.to.MovieTo
import com.movies.list.movies.to.PagedMovie
import com.movies.list.utils.GraphQLRestTemplate
import com.neovisionaries.i18n.CountryCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class MovieQueryResolverTest : AbstractTest() {
    @Autowired
    private lateinit var graphQLRestTemplate: GraphQLRestTemplate

    @Autowired
    private lateinit var moviesService: MoviesService

    @Test
    fun getMovie() {
        val movie = moviesService.createMovie(CreateMoviesTo("Name", "OName", null, "about", CountryCode.US, emptySet(), LocalDate.of(2019, 12, 27), "0+", "1:20"))

        val variables = ObjectMapper().createObjectNode()
                .apply { put("id", movie.id) }

        val response = graphQLRestTemplate.perform("graphql/get_movie_by_id.graphql", variables)
        assertTrue(response.isOk)

        val actual = response.get("$.data.movie", MovieTo::class.java)
        val expected = MovieTo(movie.id, "Name", "OName", false, "about", CountryCode.US, emptySet(), LocalDate.of(2019, 12, 27), "0+", "1:20", movie.registered)
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered")
    }

    @Test
    fun getLatestMovies() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"))
        val createdMovie2 = moviesService.createMovie(CreateMoviesTo("Test Movie 2", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"))
        val expectedMovie1 = MovieTo(createdMovie.id, "Test Movie", "Original Name", false, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered)
        val expectedMovie2 = MovieTo(createdMovie2.id, "Test Movie 2", "Original Name", false, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie2.registered)

        val response = graphQLRestTemplate.perform("graphql/get_latest_movies.graphql")
        assertTrue(response.isOk)

        with (response.get("$.data.page1", PagedMovie::class.java)) {
            assertThat(totalElements).isEqualTo(2)
            assertThat(totalPages).isEqualTo(2)
            assertThat(movies).usingElementComparatorIgnoringFields("registered").isEqualTo(listOf(expectedMovie2))
        }

        with (response.get("$.data.page2", PagedMovie::class.java)) {
            assertThat(totalElements).isEqualTo(2)
            assertThat(totalPages).isEqualTo(2)
            assertThat(movies).usingElementComparatorIgnoringFields("registered").isEqualTo(listOf(expectedMovie1))
        }
    }

    @Test
    fun findMovies() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", "Test Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"))
        val expectedMovie = MovieTo(createdMovie.id, "Test Movie", "Test Name", false, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered)

        val createdMovie2 = moviesService.createMovie(CreateMoviesTo("Original Movie", "Original Movie", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"))
        val expectedMovie2 = MovieTo(createdMovie2.id, "Original Movie", "Original Movie", false, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie2.registered)

        val response = graphQLRestTemplate.perform("graphql/find_movie.graphql")
        assertTrue(response.isOk)

        with (response.get("$.data.byName", PagedMovie::class.java)) {
            assertThat(totalElements).isEqualTo(1)
            assertThat(totalPages).isEqualTo(1)
            assertThat(movies).usingElementComparatorIgnoringFields("registered").isEqualTo(listOf(expectedMovie))
        }

        with (response.get("$.data.byOriginalName", PagedMovie::class.java)) {
            assertThat(totalElements).isEqualTo(1)
            assertThat(totalPages).isEqualTo(1)
            assertThat(movies).usingElementComparatorIgnoringFields("registered").isEqualTo(listOf(expectedMovie2))
        }
    }
}
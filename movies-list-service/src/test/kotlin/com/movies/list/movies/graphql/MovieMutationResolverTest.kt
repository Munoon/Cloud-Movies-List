package com.movies.list.movies.graphql

import com.movies.list.AbstractTest
import com.movies.list.movies.Movie
import com.movies.list.movies.MoviesGenres
import com.movies.list.movies.MoviesRepository
import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.MovieTo
import com.movies.list.utils.GraphQLRestTemplate
import com.neovisionaries.i18n.CountryCode
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.common.OAuth2AccessToken
import java.time.LocalDate

class MovieMutationResolverTest : AbstractTest() {
    @Autowired
    private lateinit var graphQLRestTemplate: GraphQLRestTemplate

    @Autowired
    private lateinit var moviesRepository: MoviesRepository

    @Test
    fun addMovie() {
        val accessToken = getAccessToken()
        val headers = HttpHeaders()
                .apply { add("Authorization", "${OAuth2AccessToken.BEARER_TYPE} ${accessToken.value}") }

        val response = graphQLRestTemplate.perform("graphql/create_movie.graphql", headers = headers)
        assertTrue(response.isOk)
        val createdMovie = response.get("$.data.addMovie", MovieTo::class.java)

        val expected = Movie(createdMovie.id, "Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered)
        assertMatch(moviesRepository.findAll(), expected)
    }
}
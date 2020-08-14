package com.movies.list.movies

import com.movies.list.AbstractTest
import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.CreateMoviesTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class MoviesServiceTest : AbstractTest() {
    @Autowired
    private lateinit var moviesService: MoviesService;

    @Autowired
    private lateinit var moviesRepository: MoviesRepository

    @Test
    fun createMovie() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie"))
        val expected = Movie(createdMovie.id, "Test Movie", createdMovie.registered)
        assertMatch(moviesRepository.findAll(), expected)
    }
}
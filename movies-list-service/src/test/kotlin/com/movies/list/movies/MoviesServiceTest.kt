package com.movies.list.movies

import com.movies.list.AbstractTest
import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.CreateMoviesTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

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

    @Test
    internal fun getById() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie"))
        val movie = moviesService.getById(createdMovie.id!!);
        assertMatch(movie, Movie(createdMovie.id, "Test Movie", createdMovie.registered))
    }

    @Test
    internal fun getPage() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie"))
        val createdMovie2 = moviesService.createMovie(CreateMoviesTo("Test Movie 2"))

        val moviesPage1 = moviesService.getPage(PageRequest.of(0, 1))
        assertMatch(moviesPage1.content, Movie(createdMovie2.id, "Test Movie 2", createdMovie2.registered))

        val moviesPage2 = moviesService.getPage(PageRequest.of(1, 1))
        assertMatch(moviesPage2.content, Movie(createdMovie.id, "Test Movie", createdMovie.registered))
    }
}
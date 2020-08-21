package com.movies.list.movies

import com.movies.list.AbstractTest
import com.movies.list.mediaTemplate.MediaTemplateService
import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.CreateMoviesTo
import com.movies.list.utils.exception.NotFoundException
import org.bson.types.Binary
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.mock.web.MockMultipartFile
import java.time.LocalDateTime

internal class MoviesServiceTest : AbstractTest() {
    @Autowired
    private lateinit var moviesService: MoviesService;

    @Autowired
    private lateinit var moviesRepository: MoviesRepository

    @Autowired
    private lateinit var templateService: MediaTemplateService

    @Test
    fun createMovie() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", null))
        val expected = Movie(createdMovie.id, "Test Movie", null, createdMovie.registered)
        assertMatch(moviesRepository.findAll(), expected)
    }

    @Test
    internal fun createMovieWithAvatar() {
        val avatarFileText = "TEST_FILE".toByteArray()
        val avatarTemplate = templateService.create(MockMultipartFile("test file", avatarFileText))

        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", avatarTemplate.id))
        val expected = Movie(createdMovie.id, "Test Movie", Binary(avatarFileText), createdMovie.registered)
        assertMatch(moviesRepository.findAll(), expected)
    }

    @Test
    internal fun createMovieAvatarNotFound() {
        val createMovie = CreateMoviesTo("Test Movie", "UNKNOWN_AVATAR_ID")
        assertThrows(NotFoundException::class.java) { moviesService.createMovie(createMovie) }
    }

    @Test
    internal fun getById() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", null))
        val movie = moviesService.getById(createdMovie.id!!);
        assertMatch(movie, Movie(createdMovie.id, "Test Movie", null, createdMovie.registered))
    }

    @Test
    internal fun getPage() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", null))
        val createdMovie2 = moviesService.createMovie(CreateMoviesTo("Test Movie 2", null))

        val moviesPage1 = moviesService.getPage(PageRequest.of(0, 1))
        assertMatch(moviesPage1.content, Movie(createdMovie2.id, "Test Movie 2", null, createdMovie2.registered))

        val moviesPage2 = moviesService.getPage(PageRequest.of(1, 1))
        assertMatch(moviesPage2.content, Movie(createdMovie.id, "Test Movie", null, createdMovie.registered))
    }
}
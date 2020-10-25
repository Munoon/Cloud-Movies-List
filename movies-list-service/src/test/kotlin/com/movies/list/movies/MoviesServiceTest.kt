package com.movies.list.movies

import com.movies.list.AbstractTest
import com.movies.list.mediaTemplate.MediaTemplateService
import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.CreateMoviesTo
import com.movies.list.utils.exception.NotFoundException
import com.neovisionaries.i18n.CountryCode
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.Binary
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.mock.web.MockMultipartFile
import java.time.LocalDate
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
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"))
        val expected = Movie(createdMovie.id, "Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered)
        assertMatch(moviesRepository.findAll(), expected)
    }

    @Test
    internal fun createMovieWithAvatar() {
        val avatarFileText = "TEST_FILE".toByteArray()
        val avatarTemplate = templateService.create(MockMultipartFile("test file", avatarFileText))

        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", "Original Name", avatarTemplate.id, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"))
        val expected = Movie(createdMovie.id, "Test Movie", "Original Name", Binary(avatarFileText), "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered)
        assertMatch(moviesRepository.findAll(), expected)
    }

    @Test
    internal fun createMovieAvatarNotFound() {
        val createMovie = CreateMoviesTo("Test Movie", "Original Name", "UNKNOWN_AVATAR_ID", "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16")
        assertThrows(NotFoundException::class.java) { moviesService.createMovie(createMovie) }
    }

    @Test
    internal fun getById() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"))
        val movie = moviesService.getById(createdMovie.id!!);
        assertMatch(movie, Movie(createdMovie.id, "Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered))
    }

    @Test
    fun getByIdNotFound() {
        assertThrows(NotFoundException::class.java) { moviesService.getById("UNKNOWN-ID") }
    }

    @Test
    internal fun getPage() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"))
        val createdMovie2 = moviesService.createMovie(CreateMoviesTo("Test Movie 2", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"))

        val moviesPage1 = moviesService.getPage(PageRequest.of(0, 1))
        assertMatch(moviesPage1.content, Movie(createdMovie2.id, "Test Movie 2", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie2.registered))

        val moviesPage2 = moviesService.getPage(PageRequest.of(1, 1))
        assertMatch(moviesPage2.content, Movie(createdMovie.id, "Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered))
    }

    @Test
    internal fun findMovieByNameAndOriginalName() {
        val createdMovie = moviesService.createMovie(CreateMoviesTo("Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"))
        val pageable = PageRequest.of(0, 10)

        with(moviesService) {
            val query = findMovieByNameAndOriginalName("test", pageable)
            assertMatch(query.content, createdMovie)
            assertThat(query.totalElements).isEqualTo(1L)
            assertThat(query.totalPages).isEqualTo(1L)
        }

        with(moviesService) {
            val query = findMovieByNameAndOriginalName("original", pageable)
            assertMatch(query.content, createdMovie)
            assertThat(query.totalElements).isEqualTo(1L)
            assertThat(query.totalPages).isEqualTo(1L)
        }
    }
}
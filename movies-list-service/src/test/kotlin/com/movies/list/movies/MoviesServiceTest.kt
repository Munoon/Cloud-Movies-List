package com.movies.list.movies

import com.movies.list.AbstractTest
import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.CreateMovieTo
import com.movies.list.utils.exception.NotFoundException
import com.neovisionaries.i18n.CountryCode
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.Binary
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.mock.web.MockPart
import java.time.LocalDate

internal class MoviesServiceTest : AbstractTest() {
    @Autowired
    private lateinit var moviesService: MoviesService;

    @Autowired
    private lateinit var moviesRepository: MoviesRepository

    @Test
    fun createMovie() {
        val createdMovie = moviesService.createMovie(CreateMovieTo("Test Movie", "Original Name", "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"), null)
        val expected = Movie(createdMovie.id, "Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered)
        assertMatch(moviesRepository.findAll(), expected)
    }

    @Test
    internal fun createMovieWithAvatar() {
        val avatarFileText = "AVATAR";
        val createMovieTo = CreateMovieTo("Test Movie", "Original Name", "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16");
        val createdMovie = moviesService.createMovie(createMovieTo, MockPart("avatar", avatarFileText.toByteArray()))
        val expected = Movie(createdMovie.id, "Test Movie", "Original Name", Binary(avatarFileText.toByteArray()), "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered)
        assertMatch(moviesRepository.findAll(), expected)
    }

    @Test
    internal fun getById() {
        val createdMovie = moviesService.createMovie(CreateMovieTo("Test Movie", "Original Name", "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"), null)
        val movie = moviesService.getById(createdMovie.id!!);
        assertMatch(movie, Movie(createdMovie.id, "Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered))
    }

    @Test
    fun getByIdNotFound() {
        assertThrows(NotFoundException::class.java) { moviesService.getById("UNKNOWN-ID") }
    }

    @Test
    internal fun getPage() {
        val createdMovie = moviesService.createMovie(CreateMovieTo("Test Movie", "Original Name", "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"), null)
        val createdMovie2 = moviesService.createMovie(CreateMovieTo("Test Movie 2", "Original Name", "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"), null)

        val moviesPage1 = moviesService.getPage(PageRequest.of(0, 1))
        assertMatch(moviesPage1.content, Movie(createdMovie2.id, "Test Movie 2", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie2.registered))

        val moviesPage2 = moviesService.getPage(PageRequest.of(1, 1))
        assertMatch(moviesPage2.content, Movie(createdMovie.id, "Test Movie", "Original Name", null, "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16", createdMovie.registered))
    }

    @Test
    internal fun findMovieByNameAndOriginalName() {
        val createdMovie = moviesService.createMovie(CreateMovieTo("Test Movie", "Original Name", "About", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2020, 2, 7), "16+", "1:16"), null)
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
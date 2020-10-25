package com.movies.list.controllers

import com.movies.common.movie.SmallMovieTo
import com.movies.list.movies.Movie
import com.movies.list.movies.MoviesGenres
import com.movies.list.movies.MoviesRepository
import com.movies.list.movies.MoviesTestData.jsonContent
import com.neovisionaries.i18n.CountryCode
import org.bson.types.Binary
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when` as mockWhen
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

internal class MovieControllerTest : AbstractWebTest() {
    @MockBean
    private lateinit var moviesRepository: MoviesRepository

    @Test
    fun getMovieById() {
        val movie = Movie("TEST_MOVIE", "Test Name", "Original Name", Binary(ByteArray(1)), null, CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "16+", "13:47", LocalDateTime.now());
        mockWhen(moviesRepository.findById(movie.id!!)).thenReturn(Optional.of(movie));
        mockMvc.perform(get("/movie/${movie.id}"))
                .andExpect(status().isOk)
                .andExpect(jsonContent(SmallMovieTo("TEST_MOVIE", "Test Name", true, null)))
    }

    @Test
    fun getMovieByIdNotFound() {
        val movieId = "UNKNOWN-ID";
        mockWhen(moviesRepository.findById(movieId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/movie/$movieId"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun getMovieImage() {
        val fileText = "TEST FILE".toByteArray()
        val movie = Movie("TEST_MOVIE", "Test Name", "Original Name", Binary(fileText), null, CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "16+", "13:47", LocalDateTime.now());
        mockWhen(moviesRepository.findById(movie.id!!)).thenReturn(Optional.of(movie));

        mockMvc.perform(get("/movie/${movie.id}/avatar"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(fileText))
    }

    @Test
    fun getMovieImageNotFound() {
        val movieId = "UNKNOWN-ID";
        mockWhen(moviesRepository.findById(movieId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/movie/$movieId/avatar"))
                .andExpect(status().isNotFound)
    }
}
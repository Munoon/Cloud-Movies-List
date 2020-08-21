package com.movies.list.controllers

import com.movies.common.movie.MovieTo
import com.movies.list.movies.Movie
import com.movies.list.movies.MoviesService
import com.movies.list.movies.MoviesTestData.jsonContent
import org.bson.types.Binary
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

internal class MovieControllerTest : AbstractWebTest() {
    @MockBean
    private lateinit var moviesService: MoviesService

    @Test
    fun getMovieById() {
        val movie = Movie("TEST_MOVIE", "Test Name", Binary(ByteArray(1)), LocalDateTime.now());
        `when`(moviesService.getById(movie.id!!)).thenReturn(movie)
        mockMvc.perform(get("/movie/${movie.id}"))
                .andExpect(status().isOk)
                .andExpect(jsonContent(MovieTo("TEST_MOVIE", "Test Name", true, null)))
    }

    @Test
    internal fun getMovieImage() {
        val fileText = "TEST FILE".toByteArray()
        val movie = Movie("TEST_MOVIE", "Test Name", Binary(fileText), LocalDateTime.now());
        `when`(moviesService.getById(movie.id!!)).thenReturn(movie)

        mockMvc.perform(get("/movie/${movie.id}/avatar"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(fileText))
    }
}
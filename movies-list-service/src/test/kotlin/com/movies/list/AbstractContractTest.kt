package com.movies.list

import com.movies.list.controllers.AbstractWebTest
import com.movies.list.movies.Movie
import com.movies.list.movies.MoviesGenres
import com.movies.list.movies.MoviesService
import com.movies.list.utils.exception.NotFoundException
import com.neovisionaries.i18n.CountryCode
import io.restassured.config.EncoderConfig
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import org.mockito.Mockito.`when` as mockWhen

@RunWith(SpringRunner::class)
abstract class AbstractContractTest : AbstractWebTest() {
    @MockBean
    private lateinit var moviesService: MoviesService

    @Before
    fun setup() {
        val mockMvc = getNewMockMvc()
        RestAssuredMockMvc.mockMvc(mockMvc)
        RestAssuredMockMvc.config = RestAssuredMockMvcConfig()
                .encoderConfig(EncoderConfig(StandardCharsets.UTF_8.name(), StandardCharsets.UTF_8.name()))

        val movie1 = Movie("Movie-Id-1", "Example Movie", "Original Name", null, "about", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.of(2019, 12, 27), "0+", "1:30", LocalDateTime.of(2019, 12, 22, 12, 0))
        mockWhen(moviesService.getById(movie1.id!!)).thenReturn(movie1)
        mockWhen(moviesService.getById("Movie-Id-NotFound")).thenThrow(NotFoundException("Movie with id Movie-Id-NotFound not found!"));
    }
}
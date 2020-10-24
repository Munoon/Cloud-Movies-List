package com.movies.gateway.controller;

import com.movies.common.movie.SmallMovieTo;
import com.movies.gateway.AbstractTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MoviesControllerTest extends AbstractTest {
    @Test
    void movie() throws Exception {
        final String movieId = "Movie-Id-1";
        SmallMovieTo smallMovieTo = new SmallMovieTo(movieId, "Example Movie", false, LocalDateTime.of(2019, 12, 22, 12, 0));
        mockMvc.perform(get("/movie/" + movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("movie_page"))
                .andExpect(model().attribute("movie", smallMovieTo));
    }
}
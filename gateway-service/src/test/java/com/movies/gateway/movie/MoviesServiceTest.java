package com.movies.gateway.movie;

import com.movies.common.movie.SmallMovieTo;
import com.movies.gateway.AbstractTest;
import com.movies.gateway.utils.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MoviesServiceTest extends AbstractTest {
    @Autowired
    private MoviesService moviesService;

    @Test
    void getMovieById() {
        final String movieId = "Movie-Id-1";
        SmallMovieTo expected = new SmallMovieTo(movieId, "Example Movie", false, LocalDateTime.of(2019, 12, 22, 12, 0));
        SmallMovieTo actual = moviesService.getMovieById(movieId);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void getMovieByIdNotFound() {
        assertThrows(NotFoundException.class, () -> moviesService.getMovieById("Movie-Id-NotFound"));
    }
}
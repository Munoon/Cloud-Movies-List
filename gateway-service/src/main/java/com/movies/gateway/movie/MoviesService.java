package com.movies.gateway.movie;

import com.movies.common.movie.SmallMovieTo;
import com.movies.gateway.utils.exception.NotFoundException;
import com.movies.gateway.utils.exception.RequestException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MoviesService {
    private MovieClient movieClient;

    public SmallMovieTo getMovieById(String movieId) {
        try {
            return movieClient.getMovieById(movieId);
        } catch (FeignException e) {
            if (e.status() == -1) {
                throw e;
            }

            HttpStatus status = HttpStatus.valueOf(e.status());

            if (status.equals(HttpStatus.NOT_FOUND)) {
                String message = String.format("Movie with id '%s' is not found!", movieId);
                throw new NotFoundException(message);
            } else {
                String message = String.format("Error request movie with id '%s'.", movieId);
                throw new RequestException(message, status);
            }
        }
    }
}

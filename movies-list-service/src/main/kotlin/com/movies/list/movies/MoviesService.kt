package com.movies.list.movies

import com.movies.list.movies.to.CreateMoviesTo
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class MoviesService(private val moviesRepository: MoviesRepository) {
    fun createMovie(createMoviesTo: CreateMoviesTo): Movie {
        val movie = MovieMapper.INSTANCE.asMovie(createMoviesTo);
        return moviesRepository.save(movie);
    }

    fun getById(movieId: String): Movie {
        return moviesRepository.findById(movieId)
                .orElseThrow { RuntimeException("Movie with id $movieId not found!") }
    }
}
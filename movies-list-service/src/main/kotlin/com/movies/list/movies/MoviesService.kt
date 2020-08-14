package com.movies.list.movies

import com.movies.list.movies.to.CreateMoviesTo
import org.springframework.stereotype.Service

@Service
class MoviesService(private val moviesRepository: MoviesRepository) {
    fun createMovie(createMoviesTo: CreateMoviesTo): Movie {
        val movie = MovieMapper.INSTANCE.asMovie(createMoviesTo);
        return moviesRepository.save(movie);
    }
}
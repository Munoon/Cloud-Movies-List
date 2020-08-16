package com.movies.list.movies

import com.movies.list.movies.to.CreateMoviesTo
import com.movies.list.utils.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MoviesService(private val moviesRepository: MoviesRepository) {
    fun createMovie(createMoviesTo: CreateMoviesTo): Movie {
        val movie = MovieMapper.INSTANCE.asMovie(createMoviesTo);
        return moviesRepository.save(movie);
    }

    fun getById(movieId: String): Movie {
        return moviesRepository.findById(movieId)
                .orElseThrow { NotFoundException("Movie with id $movieId not found!") }
    }

    fun getPage(pageable: Pageable): Page<Movie> {
        return moviesRepository.findAll(pageable)
    }
}
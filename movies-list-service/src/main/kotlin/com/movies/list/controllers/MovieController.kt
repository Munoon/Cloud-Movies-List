package com.movies.list.controllers

import com.movies.common.movie.MovieTo
import com.movies.list.movies.MovieMapper
import com.movies.list.movies.MoviesService
import com.movies.list.utils.SecurityUtils
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MovieController(private val moviesService: MoviesService) {
    private val log = LoggerFactory.getLogger(MovieController::class.java)

    @GetMapping("/movie/{id}")
    fun getMovieById(@PathVariable id: String): MovieTo {
        log.info("User ${SecurityUtils.authUserIdOrAnonymous()} get movie with id $id")
        val movie = moviesService.getById(id)
        return MovieMapper.INSTANCE.asMovieTo(movie)
    }
}
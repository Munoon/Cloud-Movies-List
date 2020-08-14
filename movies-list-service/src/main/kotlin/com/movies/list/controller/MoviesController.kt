package com.movies.list.controller

import com.movies.list.movies.Movie
import com.movies.list.movies.MoviesService
import com.movies.list.movies.to.CreateMoviesTo
import com.movies.list.utils.SecurityUtils.authUserId
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class MoviesController(private val moviesService: MoviesService) {
    private val log = LoggerFactory.getLogger(MoviesController::class.java)

    @PostMapping("/admin/movies/add")
    fun addMovie(@RequestBody @Valid createMoviesTo: CreateMoviesTo): Movie {
        log.info("Admin ${authUserId()} add movie: $createMoviesTo")
        return moviesService.createMovie(createMoviesTo)
    }

    @GetMapping("/{movieId}")
    fun getMovieById(@PathVariable movieId: String): Movie {
        log.info("User get movie $movieId")
        return moviesService.getById(movieId)
    }
}
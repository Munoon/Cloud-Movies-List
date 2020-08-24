package com.movies.list.controllers

import com.movies.common.movie.SmallMovieTo
import com.movies.list.movies.MovieMapper
import com.movies.list.movies.MoviesService
import com.movies.list.utils.SecurityUtils
import com.movies.list.utils.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/movie/{id}")
class MovieController(private val moviesService: MoviesService) {
    private val log = LoggerFactory.getLogger(MovieController::class.java)

    @GetMapping
    fun getMovieById(@PathVariable id: String): SmallMovieTo {
        log.info("User ${SecurityUtils.authUserIdOrAnonymous()} get movie with id $id")
        val movie = moviesService.getById(id)
        return MovieMapper.INSTANCE.asSmallMovie(movie)
    }

    @GetMapping("/avatar")
    fun getMovieImage(@PathVariable id: String): ResponseEntity<ByteArray> {
        log.info("User ${SecurityUtils.authUserIdOrAnonymous()} get movie with id $id")
        val movie = moviesService.getById(id)

        val avatar = movie.avatar?.data
                ?: throw NotFoundException("Avatar form movie with id '$id' is not found!")

        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG

        return ResponseEntity(avatar, headers, HttpStatus.OK)
    }
}
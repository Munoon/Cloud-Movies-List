package com.movies.list.movies.graphql

import com.movies.list.movies.MoviesService
import com.movies.list.movies.asMovieTo
import com.movies.list.movies.to.CreateMovieTo
import com.movies.list.movies.to.MovieTo
import com.movies.list.utils.SecurityUtils.authUserId
import graphql.kickstart.tools.GraphQLMutationResolver
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.servlet.http.Part
import javax.validation.Valid

@Component
@Validated
class MovieMutationResolver(private val moviesService: MoviesService): GraphQLMutationResolver {
    private val log = LoggerFactory.getLogger(MovieMutationResolver::class.java)

    @PreAuthorize("hasRole('ADMIN')")
    fun addMovie(@Valid createMovieTo: CreateMovieTo, avatar: Part?): MovieTo {
        log.info("Add movie $createMovieTo by user ${authUserId()}")
        val movie = moviesService.createMovie(createMovieTo, avatar);
        return movie.asMovieTo()
    }
}
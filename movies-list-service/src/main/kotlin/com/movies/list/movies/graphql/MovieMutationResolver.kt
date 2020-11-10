package com.movies.list.movies.graphql

import com.movies.list.movies.MoviesService
import com.movies.list.movies.asMovieTo
import com.movies.list.movies.to.CreateMoviesTo
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
    fun addMovie(@Valid createMoviesTo: CreateMoviesTo, avatar: Part?): MovieTo {
        log.info("Add movie $createMoviesTo by user ${authUserId()}")
        val movie = moviesService.createMovie(createMoviesTo, avatar);
        return movie.asMovieTo()
    }
}
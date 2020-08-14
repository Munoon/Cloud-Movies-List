package com.movies.list.movies.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.movies.list.movies.Movie
import com.movies.list.movies.MoviesService
import com.movies.list.movies.to.CreateMoviesTo
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
class MovieMutationResolver(private val moviesService: MoviesService): GraphQLMutationResolver {
    private val log = LoggerFactory.getLogger(MovieMutationResolver::class.java)

    @PreAuthorize("hasRole('ADMIN')")
    fun addMovie(createMoviesTo: CreateMoviesTo): Movie {
        log.info("Add movie $createMoviesTo")
        return moviesService.createMovie(createMoviesTo);
    }
}
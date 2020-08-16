package com.movies.list.movies.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.movies.common.movie.MovieTo
import com.movies.list.movies.MovieMapper
import com.movies.list.movies.MoviesService
import com.movies.list.movies.to.CreateMoviesTo
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class MovieMutationResolver(private val moviesService: MoviesService): GraphQLMutationResolver {
    private val log = LoggerFactory.getLogger(MovieMutationResolver::class.java)

    @PreAuthorize("hasRole('ADMIN')")
    fun addMovie(createMoviesTo: CreateMoviesTo): MovieTo {
        log.info("Add movie $createMoviesTo")
        val movie = moviesService.createMovie(createMoviesTo);
        return MovieMapper.INSTANCE.asMovieTo(movie)
    }
}
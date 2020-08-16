package com.movies.list.movies.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.movies.common.movie.MovieTo
import com.movies.list.movies.MovieMapper
import com.movies.list.movies.MoviesService
import com.movies.list.utils.SecurityUtils.authUserIdOrAnonymous
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MovieQueryResolver(private val moviesService: MoviesService) : GraphQLQueryResolver {
    private val log = LoggerFactory.getLogger(MovieQueryResolver::class.java)

    fun getMovie(id: String): MovieTo {
        log.info("Get movie $id by user ${authUserIdOrAnonymous()}")
        val movie = moviesService.getById(id)
        return MovieMapper.INSTANCE.asMovieTo(movie)
    }
}
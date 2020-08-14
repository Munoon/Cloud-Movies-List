package com.movies.list.movies.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.movies.list.movies.Movie
import com.movies.list.movies.MoviesService
import com.movies.list.utils.SecurityUtils.authUserIdOrAnonymous
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MovieQueryResolver(private val moviesService: MoviesService) : GraphQLQueryResolver {
    private val log = LoggerFactory.getLogger(MovieQueryResolver::class.java)

    fun getMovie(id: String): Movie {
        log.info("Get movie $id by user ${authUserIdOrAnonymous()}")
        return moviesService.getById(id)
    }
}
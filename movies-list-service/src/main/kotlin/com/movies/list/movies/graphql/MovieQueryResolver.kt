package com.movies.list.movies.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.movies.common.movie.MovieTo
import com.movies.list.movies.MovieMapper
import com.movies.list.movies.MoviesService
import com.movies.list.utils.SecurityUtils.authUserIdOrAnonymous
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class MovieQueryResolver(private val moviesService: MoviesService) : GraphQLQueryResolver {
    private val log = LoggerFactory.getLogger(MovieQueryResolver::class.java)

    fun getMovie(id: String): MovieTo {
        log.info("Get movie $id by user ${authUserIdOrAnonymous()}")
        val movie = moviesService.getById(id)
        return MovieMapper.INSTANCE.asMovieTo(movie)
    }

    fun getLatestMovies(count: Int, page: Int): List<MovieTo> {
        log.info("Get latest movie (count: $count, page: $page) by user ${authUserIdOrAnonymous()}")
        val pageRequest = PageRequest.of(page, count)
        return moviesService.getPage(pageRequest)
                .map { MovieMapper.INSTANCE.asMovieTo(it) }
                .content
    }
}
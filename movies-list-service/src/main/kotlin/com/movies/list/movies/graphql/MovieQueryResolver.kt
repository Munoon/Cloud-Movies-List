package com.movies.list.movies.graphql

import com.movies.list.movies.Movie
import com.movies.list.movies.MovieMapper
import com.movies.list.movies.MoviesService
import com.movies.list.movies.asMovieTo
import com.movies.list.movies.to.MovieTo
import com.movies.list.movies.to.PagedMovie
import com.movies.list.utils.SecurityUtils.authUserIdOrAnonymous
import graphql.kickstart.tools.GraphQLQueryResolver
import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max

@Component
@Validated
class MovieQueryResolver(private val moviesService: MoviesService) : GraphQLQueryResolver {
    private val log = LoggerFactory.getLogger(MovieQueryResolver::class.java)

    fun getMovie(id: String): MovieTo {
        log.info("Get movie $id by user ${authUserIdOrAnonymous()}")
        val movie = moviesService.getById(id)
        return movie.asMovieTo()
    }

    fun getLatestMovies(@Max(20) count: Int, page: Int): PagedMovie {
        log.info("Get latest movie (count: $count, page: $page) by user ${authUserIdOrAnonymous()}")
        val pageRequest = PageRequest.of(page, count)
        val pageData = moviesService.getPage(pageRequest).map(Movie::asMovieTo)
        return PagedMovie(pageData.totalPages, pageData.totalElements, pageData.content)
    }

    fun findMovies(@Length(min = 1) findQuery: String, @Max(20) count: Int, page: Int): PagedMovie {
        log.info("Find movies by query '$findQuery' (count: $count, page: $page) by user ${authUserIdOrAnonymous()}")
        val pageRequest = PageRequest.of(page, count)
        val pageData = moviesService.findMovieByNameAndOriginalName(findQuery, pageRequest).map(Movie::asMovieTo)
        return PagedMovie(pageData.totalPages, pageData.totalElements, pageData.content)
    }
}
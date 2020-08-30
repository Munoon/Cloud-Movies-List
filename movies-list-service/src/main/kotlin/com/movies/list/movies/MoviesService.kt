package com.movies.list.movies

import com.movies.list.mediaTemplate.MediaTemplateService
import com.movies.list.movies.to.CreateMoviesTo
import com.movies.list.utils.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class MoviesService(
        private val moviesRepository: MoviesRepository,
        private val mediaTemplateService: MediaTemplateService
) {
    fun createMovie(createMoviesTo: CreateMoviesTo): Movie {
        var movie = MovieMapper.INSTANCE.asMovie(createMoviesTo);
        if (createMoviesTo.avatarImageId !== null) {
            val template = mediaTemplateService.getById(createMoviesTo.avatarImageId)
            movie = movie.copy(avatar = template.media)
        }
        return moviesRepository.save(movie);
    }

    fun getById(movieId: String): Movie {
        return moviesRepository.findById(movieId)
                .orElseThrow { NotFoundException("Movie with id $movieId not found!") }
    }

    fun getPage(pageable: Pageable): Page<Movie> {
        val pageRequest = PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                pageable.getSortOr(SORT_BY_REGISTERED)
        )
        return moviesRepository.findAll(pageRequest)
    }

    fun findMovieByNameAndOriginalName(query: String, pageable: Pageable): Page<Movie> {
        return moviesRepository.findByNameOrOriginalName(query, pageable)
    }

    companion object {
        private val SORT_BY_REGISTERED = Sort.by(Sort.Direction.DESC, "registered")
    }
}
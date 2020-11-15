package com.movies.list.movies

import com.movies.list.movies.to.CreateMovieTo
import com.movies.list.utils.exception.NotFoundException
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import javax.servlet.http.Part

@Service
class MoviesService(private val moviesRepository: MoviesRepository) {
    @Caching(
            put = [ CachePut(value = ["movies"], key = "#result.id") ],
            evict = [
                CacheEvict(value = ["movies_list"], allEntries = true),
                CacheEvict(value = ["movies_find"], allEntries = true),
                CacheEvict(value = ["movies_count"], allEntries = true)
            ]
    )
    fun createMovie(createMovieTo: CreateMovieTo, avatar: Part?): Movie {
        val movie = createMovieTo.asMovie(avatar)
        return moviesRepository.save(movie);
    }

    @Cacheable(value = ["movies"], key = "#movieId")
    fun getById(movieId: String): Movie {
        return moviesRepository.findById(movieId)
                .orElseThrow { NotFoundException("Movie with id $movieId not found!") }
    }

    @Cacheable("movies_list")
    fun getPage(pageable: Pageable): Page<Movie> {
        val pageRequest = PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                pageable.getSortOr(SORT_BY_REGISTERED)
        )
        return moviesRepository.findAll(pageRequest)
    }

    @Cacheable(value = ["movies_find"], key = "T(java.lang.Integer).toString(#query.hashCode()).concat('-').concat(#pageable.hashCode())")
    fun findMovieByNameAndOriginalName(query: String, pageable: Pageable): Page<Movie> {
        return moviesRepository.findByNameOrOriginalName(query, pageable)
    }

    @Cacheable("movies_count")
    fun count(): Long = moviesRepository.count()

    companion object {
        private val SORT_BY_REGISTERED = Sort.by(Sort.Direction.DESC, "registered")
    }
}
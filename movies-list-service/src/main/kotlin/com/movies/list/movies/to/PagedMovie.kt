package com.movies.list.movies.to

import com.movies.common.movie.MovieTo

data class PagedMovie(
        val totalPages: Int,
        val totalElements: Long,
        val movies: List<MovieTo>
)
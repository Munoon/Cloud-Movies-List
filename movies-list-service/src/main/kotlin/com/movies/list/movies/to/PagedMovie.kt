package com.movies.list.movies.to

data class PagedMovie(
        val totalPages: Int,
        val totalElements: Long,
        val movies: List<MovieTo>
)
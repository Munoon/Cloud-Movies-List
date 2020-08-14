package com.movies.list.movies

import com.movies.list.movies.to.CreateMoviesTo
import java.time.LocalDateTime

interface MovieMapper {
    fun asMovie(createMoviesTo: CreateMoviesTo): Movie

    companion object {
        // TODO refactor this with mapstruct
        val INSTANCE = object: MovieMapper {
            override fun asMovie(createMoviesTo: CreateMoviesTo) =
                    Movie(null, createMoviesTo.name, LocalDateTime.now())
        }
    }
}
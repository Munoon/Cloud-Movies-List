package com.movies.list.movies

import com.movies.common.movie.MovieTo
import com.movies.list.movies.to.CreateMoviesTo
import java.time.LocalDateTime

interface MovieMapper {
    fun asMovie(createMoviesTo: CreateMoviesTo): Movie
    fun asMovieTo(movie: Movie): MovieTo

    companion object {
        // TODO refactor this with mapstruct
        val INSTANCE = object: MovieMapper {
            override fun asMovie(createMoviesTo: CreateMoviesTo) =
                    Movie(null, createMoviesTo.name, null, LocalDateTime.now())

            override fun asMovieTo(movie: Movie): MovieTo =
                    MovieTo(movie.id, movie.name, movie.avatar !== null, movie.registered)
        }
    }
}
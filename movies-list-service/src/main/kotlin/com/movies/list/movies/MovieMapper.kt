package com.movies.list.movies

import com.movies.common.movie.SmallMovieTo
import com.movies.list.movies.to.CreateMoviesTo
import com.movies.list.movies.to.MovieTo
import java.time.LocalDateTime

interface MovieMapper {
    fun asMovie(createMoviesTo: CreateMoviesTo): Movie
    fun asMovieTo(movie: Movie): MovieTo
    fun asSmallMovie(movie: Movie): SmallMovieTo

    companion object {
        // TODO refactor this with mapstruct
        val INSTANCE = object: MovieMapper {
            override fun asMovie(createMoviesTo: CreateMoviesTo) =
                    Movie(null, createMoviesTo.name, createMoviesTo.originalName,
                            null, createMoviesTo.about, createMoviesTo.country, createMoviesTo.genres,
                            createMoviesTo.premiere, createMoviesTo.age, createMoviesTo.time, LocalDateTime.now())

            override fun asMovieTo(movie: Movie) =
                    MovieTo(movie.id, movie.name, movie.originalName,
                            movie.avatar != null, movie.about, movie.country, movie.genres,
                            movie.premiere, movie.age, movie.time, movie.registered)

            override fun asSmallMovie(movie: Movie): SmallMovieTo =
                    SmallMovieTo(movie.id, movie.name, movie.avatar !== null, movie.registered)
        }
    }
}
package com.movies.list.movies

import com.movies.common.movie.SmallMovieTo
import com.movies.list.movies.to.CreateMovieTo
import com.movies.list.movies.to.MovieTo
import org.bson.types.Binary
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import javax.servlet.http.Part

@Mapper
abstract class MovieMapper {
    @Mappings(
            Mapping(target = "id", expression = "java(null)"),
            Mapping(target = "avatar", expression = "java(null)"),
            Mapping(target = "registered", expression = "java(java.time.LocalDateTime.now())")
    )
    abstract fun asMovie(createMovieTo: CreateMovieTo): Movie

    @Mapping(target = "hasAvatar", expression = "java(movie.getAvatar() != null)")
    abstract fun asMovieTo(movie: Movie): MovieTo

    @Mapping(target = "hasAvatar", expression = "java(movie.getAvatar() != null)")
    abstract fun asSmallMovie(movie: Movie): SmallMovieTo

    fun asMovie(createMovieTo: CreateMovieTo, avatar: Part?): Movie {
        val movie = asMovie(createMovieTo)
        return if (avatar != null) movie.copy(avatar = Binary(avatar.inputStream.readAllBytes()))
            else movie
    }

    companion object {
        internal val INSTANCE = Mappers.getMapper(MovieMapper::class.java)!!
    }
}

fun CreateMovieTo.asMovie(avatar: Part?): Movie = MovieMapper.INSTANCE.asMovie(this, avatar)
fun Movie.asMovieTo(): MovieTo = MovieMapper.INSTANCE.asMovieTo(this)
fun Movie.asSmallMovie(): SmallMovieTo = MovieMapper.INSTANCE.asSmallMovie(this)
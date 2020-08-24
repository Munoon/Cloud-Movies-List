package com.movies.list.movies.to

import com.movies.list.movies.MoviesGenres
import com.neovisionaries.i18n.CountryCode
import org.bson.types.Binary
import org.springframework.data.annotation.Id
import java.time.LocalDate
import java.time.LocalDateTime

data class MovieTo(
        var id: String?,

        var name: String,

        val originalName: String?,

        val hasAvatar: Boolean,

        val about: String?,

        val country: CountryCode,

        val genres: Set<MoviesGenres>,

        val premiere: LocalDate,

        val age: String,

        val time: String,

        var registered: LocalDateTime
)
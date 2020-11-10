package com.movies.list.movies

import com.github.pozo.KotlinBuilder
import com.neovisionaries.i18n.CountryCode
import lombok.NoArgsConstructor
import org.bson.types.Binary
import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@KotlinBuilder
@NoArgsConstructor
@Document(collection = "movies")
data class Movie(
        @Id
        var id: String?,

        @NotNull
        @Length(min = 1, max = 40)
        var name: String,

        @Length(min = 1, max = 40)
        val originalName: String?,

        val avatar: Binary?,

        @Length(max = 300)
        val about: String?,

        @NotNull
        val country: CountryCode,

        @NotNull
        @Size(min = 1)
        val genres: Set<MoviesGenres>,

        @NotNull
        val premiere: LocalDate,

        @NotNull
        @Length(min = 1, max = 10)
        val age: String,

        @NotNull
        @Length(min = 1, max = 10)
        val time: String,

        @NotNull
        var registered: LocalDateTime
)
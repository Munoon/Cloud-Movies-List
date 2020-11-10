package com.movies.list.movies

import com.github.pozo.KotlinBuilder
import com.movies.list.utils.validators.media.FileSize
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

        @field:NotNull
        @field:Length(min = 1, max = 40)
        var name: String,

        @field:Length(min = 1, max = 40)
        val originalName: String?,

        @field:FileSize(max = "1MB", message = "Максимальный размер аватарки - 1 MB")
        val avatar: Binary?,

        @field:Length(max = 300)
        val about: String?,

        @field:NotNull
        val country: CountryCode,

        @field:NotNull
        @field:Size(min = 1)
        val genres: Set<MoviesGenres>,

        @field:NotNull
        val premiere: LocalDate,

        @field:NotNull
        @field:Length(min = 1, max = 10)
        val age: String,

        @field:NotNull
        @field:Length(min = 1, max = 10)
        val time: String,

        @field:NotNull
        var registered: LocalDateTime
)
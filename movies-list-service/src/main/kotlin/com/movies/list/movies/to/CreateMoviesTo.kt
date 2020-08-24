package com.movies.list.movies.to

import com.movies.list.movies.MoviesGenres
import com.neovisionaries.i18n.CountryCode
import lombok.NoArgsConstructor
import org.hibernate.validator.constraints.Length
import java.time.LocalDate
import javax.validation.constraints.Size

@NoArgsConstructor
data class CreateMoviesTo(
        @Length(min = 2)
        val name: String,

        @Length(min = 1, max = 40)
        val originalName: String?,

        val avatarImageId: String?,

        @Length(max = 300)
        val about: String?,

        val country: CountryCode,

        @Size(min = 1)
        val genres: Set<MoviesGenres>,

        val premiere: LocalDate,

        @Length(min = 1, max = 10)
        val age: String,

        @Length(min = 1, max = 10)
        val time: String
)
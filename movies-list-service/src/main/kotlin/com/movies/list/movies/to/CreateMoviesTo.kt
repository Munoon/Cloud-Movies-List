package com.movies.list.movies.to

import lombok.NoArgsConstructor
import org.hibernate.validator.constraints.Length

@NoArgsConstructor
data class CreateMoviesTo(
        @Length(min = 2)
        val name: String,

        val avatarImageId: String?
)
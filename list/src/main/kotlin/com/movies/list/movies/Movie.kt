package com.movies.list.movies

import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "movies")
@NoArgsConstructor
data class Movie(
        @Id
        var id: String?,

        var name: String,

        var registered: LocalDateTime
)
package com.movies.list.movies

import lombok.NoArgsConstructor
import org.bson.types.Binary
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "movies")
@NoArgsConstructor
data class Movie(
        @Id
        var id: String?,

        var name: String,

        val avatar: Binary?,

        var registered: LocalDateTime
)
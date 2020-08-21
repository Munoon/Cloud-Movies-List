package com.movies.list.mediaTemplate

import org.bson.types.Binary
import java.time.LocalDateTime

data class MediaTemplate(
        val id: String?,

        val media: Binary,

        val registered: LocalDateTime
)
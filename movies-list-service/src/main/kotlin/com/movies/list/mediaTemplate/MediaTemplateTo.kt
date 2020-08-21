package com.movies.list.mediaTemplate

import lombok.NoArgsConstructor
import java.time.LocalDateTime

@NoArgsConstructor
data class MediaTemplateTo (
        val id: String?,
        val registered: LocalDateTime
)
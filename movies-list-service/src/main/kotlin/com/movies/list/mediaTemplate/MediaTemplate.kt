package com.movies.list.mediaTemplate

import com.movies.list.utils.validators.media.FileSize
import org.bson.types.Binary
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class MediaTemplate(
        val id: String?,

        @NotNull
        @FileSize(max = "1MB", message = "Максимальный размер файла - 1 MB")
        val media: Binary,

        @NotNull
        val registered: LocalDateTime
)
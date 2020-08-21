package com.movies.list.mediaTemplate

import com.movies.list.utils.exception.NotFoundException
import org.bson.BsonBinarySubType
import org.bson.types.Binary
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class MediaTemplateService(private val repository: MediaTemplateRepository) {
    fun create(file: MultipartFile): MediaTemplate {
        val binary = Binary(BsonBinarySubType.BINARY, file.bytes)
        val template = MediaTemplate(null, binary, LocalDateTime.now())
        return repository.save(template)
    }

    fun getById(id: String): MediaTemplate {
        return repository.findById(id)
                .orElseThrow { NotFoundException("Media template with id '$id' is not found") }
    }
}
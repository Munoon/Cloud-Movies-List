package com.movies.list.utils.validators.media

import com.movies.list.AbstractTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import javax.validation.Validator

class MultipartFileContentTypeValidatorTest : AbstractTest() {
    @Autowired
    private lateinit var validator: Validator

    @Test
    fun testValid() {
        val data = TestData(MockMultipartFile("file", "file.img", "image/png", ByteArray(0)))
        val result = validator.validate(data)
        assertThat(result).hasSize(0)
    }

    @Test
    fun testInvalid() {
        val data = TestData(MockMultipartFile("file", "file.img", "image/jpeg", ByteArray(0)))
        val result = validator.validate(data)
        assertThat(result).hasSize(1)
    }

    private data class TestData(
            @field:ContentType(["image/png"])
            val value: MultipartFile
    )
}
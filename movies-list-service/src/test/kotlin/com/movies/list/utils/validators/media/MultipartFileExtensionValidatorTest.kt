package com.movies.list.utils.validators.media

import com.movies.list.AbstractTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import javax.validation.Validator

class MultipartFileExtensionValidatorTest : AbstractTest() {
    @Autowired
    private lateinit var validator: Validator

    @Test
    fun testValid() {
        val data = TestObject(MockMultipartFile("test.png", "test.png", "png", ByteArray(0)))
        val result = validator.validate(data)
        assertThat(result).hasSize(0)
    }

    @Test
    fun testInvalid() {
        val data = TestObject(MockMultipartFile("test.jpeg", "test.jpeg", "jpeg", ByteArray(0)))
        val result = validator.validate(data)
        assertThat(result).hasSize(1)
    }

    @Test
    fun testNoExtension() {
        val data = TestObject(MockMultipartFile("test", ByteArray(0)))
        val result = validator.validate(data)
        assertThat(result).hasSize(1)
    }

    private data class TestObject(
            @field:FileExtension(["png"])
            val file: MultipartFile
    )
}
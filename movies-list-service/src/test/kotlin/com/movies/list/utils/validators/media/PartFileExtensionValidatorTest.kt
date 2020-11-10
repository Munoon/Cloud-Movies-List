package com.movies.list.utils.validators.media

import com.movies.list.AbstractTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.mock.web.MockPart
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.Part
import javax.validation.Validator

class PartFileExtensionValidatorTest : AbstractTest() {
    @Autowired
    private lateinit var validator: Validator

    @Test
    fun testValid() {
        val data = TestObject(MockPart("test.png", "test.png", ByteArray(0)))
        val result = validator.validate(data)
        assertThat(result).hasSize(0)
    }

    @Test
    fun testInvalid() {
        val data = TestObject(MockPart("test.jpeg", "test.jpeg", ByteArray(0)))
        val result = validator.validate(data)
        assertThat(result).hasSize(1)
    }

    @Test
    fun testNoExtension() {
        val data = TestObject(MockPart("test", ByteArray(0)))
        val result = validator.validate(data)
        assertThat(result).hasSize(1)
    }

    private data class TestObject(
            @field:FileExtension(["png"])
            val file: Part
    )
}
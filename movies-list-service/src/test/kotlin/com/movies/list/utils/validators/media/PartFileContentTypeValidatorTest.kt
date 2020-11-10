package com.movies.list.utils.validators.media

import com.movies.list.AbstractTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.spy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockPart
import javax.servlet.http.Part
import javax.validation.Validator
import org.mockito.Mockito.`when` as mockWhen

class PartFileContentTypeValidatorTest : AbstractTest() {
    @Autowired
    private lateinit var validator: Validator

    @Test
    fun testValid() {
        val mockPart = spy(MockPart("file", "file.img", ByteArray(0)))
        mockWhen(mockPart.contentType).thenReturn("image/png")
        val data = TestData(mockPart)
        val result = validator.validate(data)
        assertThat(result).hasSize(0)
    }

    @Test
    fun testInvalid() {
        val mockPart = spy(MockPart("file", "file.img", ByteArray(0)))
        mockWhen(mockPart.contentType).thenReturn("image/jpeg")
        val data = TestData(mockPart)
        val result = validator.validate(data)
        assertThat(result).hasSize(1)
    }

    private data class TestData(
            @field:ContentType(["image/png"])
            val value: Part
    )
}
package com.movies.list.utils.validators.media.size

import com.movies.list.AbstractTest
import com.movies.list.utils.validators.media.FileSize
import org.assertj.core.api.Assertions
import org.bson.types.Binary
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.validation.Validator

class MultipartFileSizeValidatorTest : AbstractTest() {
    @Autowired
    private lateinit var validator: Validator

    @Test
    fun testValid() {
        val testData = TestData(Binary(ByteArray(15)))
        val result = validator.validate(testData)
        Assertions.assertThat(result).hasSize(0)
    }

    @Test
    fun testInvalidLess() {
        val testData = TestData(Binary(ByteArray(5)))
        val result = validator.validate(testData)
        Assertions.assertThat(result).hasSize(1)
    }

    @Test
    fun testInvalidMore() {
        val testData = TestData(Binary(ByteArray(25)))
        val result = validator.validate(testData)
        Assertions.assertThat(result).hasSize(1)
    }

    @Test
    fun testMaxNull() {
        val testData = TestDataMaxNull(Binary(ByteArray(15)))
        val result = validator.validate(testData)
        Assertions.assertThat(result).hasSize(0)
    }

    @Test
    fun testInvalidMaxNull() {
        val testData = TestDataMaxNull(Binary(ByteArray(5)))
        val result = validator.validate(testData)
        Assertions.assertThat(result).hasSize(1)
    }

    private data class TestData(
            @field:FileSize(min = "10B", max = "20B")
            val value: Binary
    )

    private data class TestDataMaxNull(
            @field:FileSize(min = "10B")
            val value: Binary
    )
}
package com.movies.list.mediaTemplate

import com.movies.list.AbstractTest
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.Binary
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import java.io.ByteArrayInputStream

internal class MediaTemplateServiceTest : AbstractTest() {
    @Autowired
    private lateinit var mediaTemplateService: MediaTemplateService

    @Autowired
    private lateinit var mediaTemplateRepository: MediaTemplateRepository

    private val fileTextByteArray = "FILE TEXT!".toByteArray()
    private lateinit var globalMediaTemplate: MediaTemplate

    @BeforeEach
    fun createFile() {
        val file = MockMultipartFile("test file", ByteArrayInputStream(fileTextByteArray))
        globalMediaTemplate = mediaTemplateService.create(file)
    }

    @Test
    fun create() {
        val expected = arrayListOf(MediaTemplate(globalMediaTemplate.id, Binary(fileTextByteArray), globalMediaTemplate.registered))
        assertThat(mediaTemplateRepository.findAll()).usingElementComparatorIgnoringFields("registered").isEqualTo(expected)
    }

    @Test
    fun getById() {
        val actual = mediaTemplateService.getById(globalMediaTemplate.id!!)
        val expected = MediaTemplate(globalMediaTemplate.id, Binary(fileTextByteArray), globalMediaTemplate.registered)
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered")
    }
}
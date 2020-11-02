package com.movies.list.controllers

import com.movies.list.mediaTemplate.MediaTemplate
import com.movies.list.mediaTemplate.MediaTemplateRepository
import com.movies.list.mediaTemplate.MediaTemplateTo
import com.movies.list.utils.JsonUtil
import org.assertj.core.api.Assertions
import org.bson.types.Binary
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class MediaTemplateControllerTest : AbstractWebTest() {
    @Autowired
    private lateinit var mediaTemplateRepository: MediaTemplateRepository

    @Test
    fun createTemplate() {
        val fileByteArray = "FILE TEXT".toByteArray()
        val file = MockMultipartFile("file", "image.png", "image/png", fileByteArray)

        val result = mockMvc.perform(multipart("/templates/create")
                .file(file)
                .with(authUser()))
                .andExpect(status().isOk)
                .andReturn()

        val createdMediaTemplate = JsonUtil.readValue(JsonUtil.getContent(result), MediaTemplateTo::class.java)

        val expected = arrayListOf(MediaTemplate(createdMediaTemplate.id, Binary(fileByteArray), createdMediaTemplate.registered))
        Assertions.assertThat(mediaTemplateRepository.findAll()).usingElementComparatorIgnoringFields("registered").isEqualTo(expected)
    }
}
package com.movies.list.controllers

import com.movies.list.mediaTemplate.MediaTemplateMapper
import com.movies.list.mediaTemplate.MediaTemplateService
import com.movies.list.mediaTemplate.MediaTemplateTo
import com.movies.list.utils.SecurityUtils.authUserId
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/templates")
class MediaTemplateController(private val mediaTemplateService: MediaTemplateService) {
    private val log = LoggerFactory.getLogger(MediaTemplateController::class.java)

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    fun createTemplate(@RequestParam("file") file: MultipartFile): MediaTemplateTo {
        val template = mediaTemplateService.create(file)
        log.info("User ${authUserId()} create template ${template.id}")
        return MediaTemplateMapper.INSTANCE.asTo(template)
    }
}
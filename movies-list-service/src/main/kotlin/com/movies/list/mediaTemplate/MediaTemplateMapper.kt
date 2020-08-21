package com.movies.list.mediaTemplate

interface MediaTemplateMapper {
    fun asTo(mediaTemplate: MediaTemplate): MediaTemplateTo

    companion object {
        val INSTANCE = object : MediaTemplateMapper {
            override fun asTo(mediaTemplate: MediaTemplate) = MediaTemplateTo(mediaTemplate.id, mediaTemplate.registered)
        }
    }
}
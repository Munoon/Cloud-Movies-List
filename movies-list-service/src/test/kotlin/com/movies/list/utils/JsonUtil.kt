package com.movies.list.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.test.web.servlet.MvcResult
import java.io.IOException

object JsonUtil {
    private val objectMapper = ObjectMapper().also {
        it.registerModule(JavaTimeModule())
    }

    fun getContent(result: MvcResult) = result.response.contentAsString;

    fun <T> readValue(json: String, clazz: Class<T>?): T {
        try {
            return objectMapper.readValue(json, clazz)
        } catch (e: IOException) {
            throw IllegalArgumentException("Invalid read from JSON:\n'$json'", e)
        }
    }
}
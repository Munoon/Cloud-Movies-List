package com.movies.list.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.*


@EnableWebMvc
@Configuration
class MvcConfig : WebMvcConfigurer {
    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper();

        objectMapper.registerModule(JavaTimeModule())
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

        OBJECT_MAPPER = objectMapper;

        return objectMapper;
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(MappingJackson2HttpMessageConverter(objectMapper()))
        converters.add(byteArrayHttpMessageConverter())
    }

    @Bean
    fun byteArrayHttpMessageConverter(): ByteArrayHttpMessageConverter {
        val arrayHttpMessageConverter = ByteArrayHttpMessageConverter()
        arrayHttpMessageConverter.supportedMediaTypes = getSupportedMediaTypes()
        return arrayHttpMessageConverter
    }

    @Bean
    fun uploadScalar(): GraphQLScalarType {
        return ApolloScalars.Upload
    }

    private fun getSupportedMediaTypes(): List<MediaType> {
        val list: MutableList<MediaType> = ArrayList()
        list.add(MediaType.IMAGE_JPEG)
        list.add(MediaType.IMAGE_PNG)
        list.add(MediaType.APPLICATION_OCTET_STREAM)
        return list
    }

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.favorParameter(false)
                .ignoreAcceptHeader(true)
                .useRegisteredExtensionsOnly(true)
                .defaultContentType(MediaType.APPLICATION_JSON)
    }

    companion object {
        lateinit var OBJECT_MAPPER: ObjectMapper
    }
}
package com.movies.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonUtil {
    private static final JacksonJsonParser JACKSON_JSON_PARSER = new JacksonJsonParser();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> String writeValue(T obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid write to JSON:\n'" + obj + "'", e);
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + "'", e);
        }
    }

    public static Map<String, Object> readValueAsMap(String json) {
        return JACKSON_JSON_PARSER.parseMap(json);
    }

    public static <T> T readFromJson(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return readValue(getContent(result), clazz);
    }

    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }
}

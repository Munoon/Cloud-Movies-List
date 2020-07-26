package com.movies.user.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movies.user.config.MvcConfig;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class JsonUtil {
    public static <T> String writeValue(T obj) {
        try {
            return MvcConfig.OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid write to JSON:\n'" + obj + "'", e);
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return MvcConfig.OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + "'", e);
        }
    }

    public static <T> T readFromJson(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return readValue(getContent(result), clazz);
    }

    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }
}

package com.movies.common.error;

import org.springframework.validation.FieldError;

import java.util.*;

public class ErrorUtils {
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static Map<String, List<String>> getErrorsFieldMap(List<FieldError> errors) {
        Map<String, List<String>> map = new HashMap<>();
        for (FieldError error : errors) {
            map.computeIfAbsent(error.getField(), k -> new ArrayList<>());
            map.get(error.getField()).add(error.getDefaultMessage());
        }
        return map;
    }
}

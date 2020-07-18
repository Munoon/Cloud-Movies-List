package com.movies.common.error;

import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class ErrorInfoField extends ErrorInfo {
    private final Map<String, List<String>> fields;

    public ErrorInfoField(CharSequence url, List<String> details, Map<String, List<String>> fields) {
        super(url, ErrorType.VALIDATION_ERROR, details);
        this.fields = fields;
    }

    public ErrorInfoField(CharSequence url, String detail, Map<String, List<String>> fields) {
        super(url, ErrorType.VALIDATION_ERROR, detail);
        this.fields = fields;
    }

    public ErrorInfoField(CharSequence url, Map<String, List<String>> fields) {
        super(url, ErrorType.VALIDATION_ERROR, Collections.emptyList());
        this.fields = fields;
    }
}

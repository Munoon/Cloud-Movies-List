package com.movies.common.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class ErrorInfoField extends ErrorInfo {
    private final Map<String, List<String>> fields;

    @JsonCreator
    public ErrorInfoField(@JsonProperty("url") CharSequence url,
                          @JsonProperty("details") List<String> details,
                          @JsonProperty("fields") Map<String, List<String>> fields) {
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

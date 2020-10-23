package com.movies.common.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class ErrorInfo implements Serializable {
    private final String url;
    private final ErrorType type;
    private final List<String> details;

    @JsonCreator
    public ErrorInfo(@JsonProperty("url") CharSequence url,
                     @JsonProperty("type") ErrorType type,
                     @JsonProperty("details") List<String> details) {
        this.url = url == null ? null : url.toString();
        this.type = type;
        this.details = details;
    }

    public ErrorInfo(CharSequence url, ErrorType type, String detail) {
        this.url = url == null ? null : url.toString();
        this.type = type;
        this.details = Collections.singletonList(detail);
    }
}

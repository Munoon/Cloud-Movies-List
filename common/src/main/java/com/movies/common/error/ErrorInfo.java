package com.movies.common.error;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
public class ErrorInfo implements Serializable {
    private final String url;
    private final ErrorType type;
    private final List<String> details;

    public ErrorInfo(CharSequence url, ErrorType type, List<String> details) {
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

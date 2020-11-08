package com.movies.gateway.utils.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RequestException extends RuntimeException {
    private final HttpStatus responseStatus;

    public RequestException(String message, HttpStatus responseStatus) {
        super(message);
        this.responseStatus = responseStatus;
    }

    public RequestException(String message, HttpStatus responseStatus, Throwable cause) {
        super(message, cause);
        this.responseStatus = responseStatus;
    }
}

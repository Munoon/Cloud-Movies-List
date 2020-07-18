package com.movies.common.error;

import java.io.Serializable;

public enum ErrorType implements Serializable {
    VALIDATION_ERROR,
    DATA_ERROR,
    NOT_UNIQUE_ERROR,
    APPLICATION_EXCEPTION
}

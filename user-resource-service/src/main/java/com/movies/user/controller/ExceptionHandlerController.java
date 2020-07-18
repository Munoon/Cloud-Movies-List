package com.movies.user.controller;

import com.movies.common.error.ErrorInfo;
import com.movies.common.error.ErrorInfoField;
import com.movies.common.error.ErrorType;
import com.movies.common.error.ErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionHandlerController {
    private static final Map<String, String> DATABASE_ERROR_MAP = Map.of(
        "users_email_key", "Пользователь с таким Email адресом уже зарегистрирован"
    );

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        Throwable rootCause = ErrorUtils.getRootCause(e);
        String constraintName = ((ConstraintViolationException) e.getCause()).getConstraintName();
        log.warn("DataIntegrityViolationException on request {}", req.getRequestURL(), rootCause);
        if (constraintName == null) {
            return new ErrorInfo(req.getRequestURL(), ErrorType.DATA_ERROR, rootCause.getMessage());
        }
        String message = DATABASE_ERROR_MAP.get(constraintName);
        return message == null
                ? new ErrorInfo(req.getRequestURL(), ErrorType.DATA_ERROR, rootCause.getMessage())
                : new ErrorInfo(req.getRequestURL(), ErrorType.NOT_UNIQUE_ERROR, message);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    public ErrorInfo beanValidationExceptionHandler(HttpServletRequest req, BindException e) {
        Map<String, List<String>> errors = ErrorUtils.getErrorsFieldMap(e.getFieldErrors());
        log.info("Binding exception on request {}", req.getRequestURL(), e);
        return new ErrorInfoField(req.getRequestURL(), errors);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo argumentNotValidHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        Map<String, List<String>> errors = ErrorUtils.getErrorsFieldMap(e.getBindingResult().getFieldErrors());
        log.info("MethodArgumentNotValid exception on request {}", req.getRequestURL(), e);
        return new ErrorInfoField(req.getRequestURL(), errors);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public ErrorInfo constraintViolationExceptionHandler(HttpServletRequest req, javax.validation.ConstraintViolationException e) {
        Map<String, List<String>> errors = new HashMap<>();
        e.getConstraintViolations().forEach(error -> {
            String key = error.getPropertyPath().toString();
            errors.computeIfAbsent(key, k -> new ArrayList<>());
            errors.get(key).add(error.getMessage());
        });
        log.info("ConstraintViolation exception on request {}", req.getRequestURL(), e);
        return new ErrorInfoField(req.getRequestURL(), errors);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo globalException(HttpServletRequest req, Exception e) {
        Throwable rootCause = ErrorUtils.getRootCause(e);
        log.warn("Exception on request {}", req.getRequestURL(), rootCause);
        return new ErrorInfo(req.getRequestURL(), ErrorType.APPLICATION_EXCEPTION, rootCause.getMessage());
    }
}

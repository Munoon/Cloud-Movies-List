package com.movies.user.controller;

import com.movies.common.error.ErrorInfo;
import com.movies.common.error.ErrorInfoField;
import com.movies.common.error.ErrorType;
import com.movies.common.util.ErrorUtils;
import com.movies.user.util.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionHandlerController {
    private static final Map<String, String> DATABASE_ERROR_MAP = Map.of(
            "users_email_key", "Пользователь с таким Email адресом уже зарегистрирован",
            "users_role_user_id_role_key", "У пользователя уже есть эта роль"
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
        log.warn("Binding exception on request {}", req.getRequestURL(), e);
        return new ErrorInfoField(req.getRequestURL(), errors);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ValidationException.class)
    public ErrorInfo validationExceptionHandler(HttpServletRequest req, ValidationException e) {
        log.warn("Validation exception on request {}", req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), ErrorType.VALIDATION_ERROR, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo argumentNotValidHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        Map<String, List<String>> errors = ErrorUtils.getErrorsFieldMap(e.getBindingResult().getFieldErrors());
        log.warn("MethodArgumentNotValid exception on request {}", req.getRequestURL(), e);
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
        log.warn("ConstraintViolation exception on request {}", req.getRequestURL(), e);
        return new ErrorInfoField(req.getRequestURL(), errors);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorInfo accessDeniedExceptionHandler(HttpServletRequest req, AccessDeniedException e) {
        log.warn("Access denied exception on request {}", req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), ErrorType.ACCESS_DENIED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(PropertyReferenceException.class)
    public ErrorInfo propertyReferenceExceptionHandler(HttpServletRequest req, PropertyReferenceException e) {
        log.warn("Property reference exception on request {}", req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), ErrorType.VALIDATION_ERROR, "You can't sort by this field");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo notFoundExceptionHandler(HttpServletRequest req, NotFoundException e) {
        log.warn("Not found exception on request {}", req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), ErrorType.NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT) // XD
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorInfo> mediaTypeExceptionHandler(HttpServletRequest req, HttpMediaTypeNotAcceptableException e) {
        log.warn("HttpMediaTypeNotAcceptable exception on request {}", req.getRequestURL(), e);
        var errorInfo = new ErrorInfo(req.getRequestURL(), ErrorType.REQUEST_EXCEPTION, "Sorry, but we support only JSON. Tea at our expense!");
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorInfo);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo globalException(HttpServletRequest req, Exception e) {
        Throwable rootCause = ErrorUtils.getRootCause(e);
        log.warn("Exception on request {}", req.getRequestURL(), rootCause);
        return new ErrorInfo(req.getRequestURL(), ErrorType.APPLICATION_EXCEPTION, rootCause.getMessage());
    }
}

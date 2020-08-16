package com.movies.gateway.controller;

import com.movies.gateway.utils.exception.NotFoundException;
import com.movies.gateway.utils.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFoundExceptionHandler(HttpServletRequest req, NotFoundException e) {
        log.error("Not found exception on request {}", req.getRequestURL(), e);
        return getErrorModel(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RequestException.class)
    public ModelAndView requestExceptionHandler(HttpServletRequest req, RequestException e) {
        log.error("Request exception on request {}", req.getRequestURL(), e);
        String message = e.getMessage() + " Server response: " + e.getResponseStatus().value();
        return getErrorModel(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView noHandlerFoundExceptionHandler(HttpServletRequest req) {
        log.warn("No handler found for request {}", req.getRequestURL());
        return getErrorModel("Страница не найдена!", HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView notFoundExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("Exception on request {}", req.getRequestURL(), e);
        return getErrorModel(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ModelAndView getErrorModel(String message, HttpStatus status) {
        Map<String, Object> params = Map.of(
                "error_message", message,
                "error_status", status.value()
        );
        return new ModelAndView("error", params);
    }
}

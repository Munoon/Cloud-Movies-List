package com.movies.list.controllers

import com.movies.common.error.ErrorInfo
import com.movies.common.error.ErrorType
import com.movies.list.utils.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ControllerExceptionHandler  {
    private val log = LoggerFactory.getLogger(ControllerExceptionHandler::class.java)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun notFoundExceptionHandler(req: HttpServletRequest, e: NotFoundException): ResponseEntity<ErrorInfo> {
        log.warn("Not found exception handler on request ${req.requestURL}", e)
        val errorInfo = ErrorInfo(req.requestURL, ErrorType.NOT_FOUND, e.message)
        return ResponseEntity(errorInfo, HttpStatus.NOT_FOUND)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun globalExceptionHandler(req: HttpServletRequest, e: Exception): ErrorInfo {
        log.warn("Exception handler on request ${req.requestURL}", e)
        return ErrorInfo(req.requestURL, ErrorType.APPLICATION_EXCEPTION, e.message)
    }
}
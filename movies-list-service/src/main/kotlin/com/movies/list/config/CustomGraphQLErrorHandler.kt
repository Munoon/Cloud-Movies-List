package com.movies.list.config

import com.movies.common.error.ErrorInfo
import com.movies.common.error.ErrorInfoField
import com.movies.common.error.ErrorType
import com.movies.list.utils.ErrorInfoGraphQLError
import com.movies.list.utils.SecurityUtils.authUserIdOrAnonymous
import com.movies.list.utils.exception.NotFoundException
import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError
import graphql.InvalidSyntaxError
import graphql.kickstart.execution.error.GraphQLErrorHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.validation.ConstraintViolationException

@Component
class CustomGraphQLErrorHandler : GraphQLErrorHandler {
    private val log = LoggerFactory.getLogger(CustomGraphQLErrorHandler::class.java)

    override fun processErrors(errors: MutableList<GraphQLError>) = errors.map(this::parseError)

    private fun parseError(error: GraphQLError): GraphQLError = when (error) {
        is ExceptionWhileDataFetching -> parseDataFetchingException(error)
        is InvalidSyntaxError -> {
            log.warn("InvalidSyntaxError on path ${error.path}, locations ${error.locations}, by user ${authUserIdOrAnonymous()}: {}", error)
            val errorInfo = ErrorInfo(null, ErrorType.REQUEST_EXCEPTION, error.message)
            ErrorInfoGraphQLError(errorInfo, error.locations, error.errorType)
        }
        else -> {
            log.error("Unknown exception by user ${authUserIdOrAnonymous()}: {}", error)
            error
        }
    }

    private fun parseDataFetchingException(error: ExceptionWhileDataFetching): GraphQLError = when (error.exception) {
        is NotFoundException -> getErrorInfo(error, ErrorType.NOT_FOUND)
        is ConstraintViolationException -> {
            log.warn("ConstraintViolationException on path ${error.path}, locations ${error.locations}, by user ${authUserIdOrAnonymous()}", error.exception)
            val url = error.path.joinToString("; ")
            val errors = (error.exception as ConstraintViolationException).constraintViolations
            val validationErrors = errors.fold(HashMap<String, MutableList<String>>()) { acc, it ->
                val key = it.propertyPath.toString()
                acc.computeIfAbsent(key) { ArrayList() }
                acc[key]!!.add(it.message)
                acc
            }
            val errorInfoField = ErrorInfoField(url, validationErrors)
            ErrorInfoGraphQLError(errorInfoField, error.locations, error.errorType)
        }
        else -> getErrorInfo(error, ErrorType.APPLICATION_EXCEPTION)
    }

    private fun getErrorInfo(error: ExceptionWhileDataFetching, errorType: ErrorType): ErrorInfoGraphQLError {
        val exceptionName = error.exception.javaClass.simpleName
        log.warn("$exceptionName on path ${error.path}, locations ${error.locations}, by user ${authUserIdOrAnonymous()}", error.exception)
        val url = error.path.joinToString("; ")
        val errorInfo = ErrorInfo(url, errorType, error.message)
        return ErrorInfoGraphQLError(errorInfo, error.locations, error.errorType)
    }
}
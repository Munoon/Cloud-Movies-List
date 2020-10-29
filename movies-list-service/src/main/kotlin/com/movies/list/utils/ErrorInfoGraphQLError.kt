package com.movies.list.utils

import com.movies.common.error.ErrorInfo
import graphql.ErrorClassification
import graphql.GraphQLError
import graphql.language.SourceLocation

data class ErrorInfoGraphQLError(private val errorInfo: ErrorInfo,
                                 private val message: String,
                                 private val locations: MutableList<SourceLocation>,
                                 private val errorType: ErrorClassification) : GraphQLError {

    constructor(errorInfo: ErrorInfo, locations: MutableList<SourceLocation>, errorType: ErrorClassification):
            this(errorInfo, errorInfo.details.joinToString("; "), locations, errorType)

    override fun getMessage() = message
    override fun getLocations() = locations
    override fun getErrorType() = errorType
    override fun getExtensions() = mutableMapOf("errorInfo" to errorInfo)
}
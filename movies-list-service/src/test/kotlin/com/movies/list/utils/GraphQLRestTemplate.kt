package com.movies.list.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.graphql.spring.boot.test.GraphQLResponse
import com.graphql.spring.boot.test.GraphQLTestTemplate
import org.springframework.http.HttpHeaders

class GraphQLRestTemplate(private val graphQLTestTemplate: GraphQLTestTemplate) {
    private val objectMapper = ObjectMapper()

    fun perform(graphQlResource: String,
                variables: ObjectNode = objectMapper.createObjectNode(),
                fragmentResources: List<String> = emptyList(),
                headers: HttpHeaders = HttpHeaders()): GraphQLResponse {
        graphQLTestTemplate.setHeaders(headers)
        val response = graphQLTestTemplate.perform(graphQlResource, variables, fragmentResources)
        graphQLTestTemplate.clearHeaders()
        return response
    }
}
package com.movies.list.utils

import com.fasterxml.jackson.databind.node.ObjectNode
import com.graphql.spring.boot.test.GraphQLResponse
import com.graphql.spring.boot.test.GraphQLTestTemplate
import org.springframework.http.HttpHeaders

class GraphQLRestTemplate(private val graphQLTestTemplate: GraphQLTestTemplate) {
    fun postForResource(graphQlResource: String, headers: HttpHeaders): GraphQLResponse {
        graphQLTestTemplate.setHeaders(headers)
        val response = graphQLTestTemplate.postForResource(graphQlResource)
        graphQLTestTemplate.clearHeaders()
        return response
    }

    fun postForResource(graphQlResource: String): GraphQLResponse = graphQLTestTemplate.postForResource(graphQlResource)
    fun perform(graphQlResource: String, variables: ObjectNode): GraphQLResponse = graphQLTestTemplate.perform(graphQlResource, variables)
}
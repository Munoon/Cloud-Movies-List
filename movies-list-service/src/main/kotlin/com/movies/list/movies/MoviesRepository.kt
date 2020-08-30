package com.movies.list.movies

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface MoviesRepository : MongoRepository<Movie, String> {
    @Query("{ \$or : [" +
                "{ name : { \$regularExpression : { pattern : ?0, options : 'i' } } }, " +
                "{ originalName : { \$regularExpression : { pattern : ?0, options : 'i' } } }" +
            "] }")
    fun findByNameOrOriginalName(query: String, pageable: Pageable): Page<Movie>
}
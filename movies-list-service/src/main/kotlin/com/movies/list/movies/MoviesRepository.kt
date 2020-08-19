package com.movies.list.movies

import org.springframework.data.mongodb.repository.MongoRepository

interface MoviesRepository : MongoRepository<Movie, String>
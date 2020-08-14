package com.movies.list

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoviesListApplication

fun main(args: Array<String>) {
    runApplication<MoviesListApplication>(*args)
}

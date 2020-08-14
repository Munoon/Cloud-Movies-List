package com.movies.list

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class MoviesListApplication

fun main(args: Array<String>) {
    runApplication<MoviesListApplication>(*args)
}

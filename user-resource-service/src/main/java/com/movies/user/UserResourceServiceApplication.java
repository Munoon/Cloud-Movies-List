package com.movies.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class UserResourceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserResourceServiceApplication.class, args);
    }

}

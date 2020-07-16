package com.movies.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableEurekaClient
@SpringBootApplication
@EnableRedisHttpSession
public class UserResourceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserResourceServiceApplication.class, args);
    }

}

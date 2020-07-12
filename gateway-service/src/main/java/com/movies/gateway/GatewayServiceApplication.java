package com.movies.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.ModelAndView;

@RestController
@EnableEurekaClient
@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Autowired
    private RestOperations restOperations;

    @GetMapping("/myInfo")
    public String person() {
        String personResourceUrl = "http://localhost:8040/info";
        return restOperations.getForObject(personResourceUrl, String.class);
    }
}

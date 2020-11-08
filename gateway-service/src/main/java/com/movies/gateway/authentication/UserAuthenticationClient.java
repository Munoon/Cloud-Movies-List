package com.movies.gateway.authentication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user-resource-service")
public interface UserAuthenticationClient {
    @RequestMapping(method = RequestMethod.POST, path = "/microservices/token/logout",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void logoutByToken(@RequestBody String tokenData);
}

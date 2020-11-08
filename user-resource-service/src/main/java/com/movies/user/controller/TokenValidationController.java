package com.movies.user.controller;

import com.movies.user.authentications.UserAuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class TokenValidationController {
    private UserAuthenticationService userAuthenticationService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/microservices/token/logout")
    public void invalidateToken(@RequestBody String token) {
        log.info("Invalidating token by microservice request");
        userAuthenticationService.invalidateToken(token);
    }
}

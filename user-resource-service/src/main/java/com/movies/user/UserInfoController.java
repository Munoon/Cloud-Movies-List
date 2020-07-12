package com.movies.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserInfoController {
    @GetMapping("/info")
    public Principal getPrincipal(Principal p) {
        return p;
    }
}

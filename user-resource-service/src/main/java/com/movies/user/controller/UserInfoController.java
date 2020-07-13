package com.movies.user.controller;

import com.movies.user.user.UserService;
import com.movies.user.user.to.RegisterUserTo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@AllArgsConstructor
public class UserInfoController {
    private UserService userService;

    @GetMapping("/info")
    public Principal getPrincipal(Principal p) {
        return p;
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterUserTo registerUserTo) {
        userService.registerUser(registerUserTo);
    }
}

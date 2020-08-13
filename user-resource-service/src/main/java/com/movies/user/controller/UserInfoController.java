package com.movies.user.controller;

import com.movies.user.user.UserService;
import com.movies.user.user.to.RegisterUserTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class UserInfoController {
    private UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterUserTo registerUserTo) {
        log.info("Register user {}", registerUserTo);
        userService.createUser(registerUserTo);
    }

    @GetMapping("/test/email/{email}")
    public boolean testEmail(@PathVariable String email) {
        log.info("Checking if user with email '{}' exist", email);
        return userService.testEmail(email);
    }
}

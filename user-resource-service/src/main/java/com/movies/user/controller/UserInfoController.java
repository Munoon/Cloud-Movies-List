package com.movies.user.controller;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.User;
import com.movies.user.user.UserService;
import com.movies.user.user.to.RegisterUserTo;
import com.movies.common.user.UserTo;
import com.movies.common.user.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class UserInfoController {
    private UserService userService;

    @GetMapping("/profile")
    public UserTo getProfile(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("Get profile of user with {}", authorizedUser.getId());
        return authorizedUser.getUserTo();
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterUserTo registerUserTo) {
        log.info("Register user {}", registerUserTo);
        userService.createUser(registerUserTo);
    }

    @GetMapping("/login/{username}")
    public User getUserForLogin(@PathVariable String username) {
        log.info("Get login info of user '{}'", username);
        return userService.getByEmail(username.toLowerCase());
    }

    @GetMapping("/test/email/{email}")
    public boolean testEmail(@PathVariable String email) {
        log.info("Checking if user with email '{}' exist", email);
        return userService.testEmail(email);
    }
}

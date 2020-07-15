package com.movies.user.controller;

import com.movies.user.user.User;
import com.movies.user.user.UserService;
import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.user.to.UserTo;
import com.movies.user.util.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserInfoController {
    private UserService userService;

    @GetMapping("/profile")
    public UserTo getProfile(@AuthenticationPrincipal OAuth2Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        User user = userService.getByEmail(email);
        return UserMapper.INSTANCE.asTo(user);
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterUserTo registerUserTo) {
        userService.registerUser(registerUserTo);
    }

    @GetMapping("/login/{username}")
    public User getUserForLogin(@PathVariable String username) {
        return userService.getByEmail(username);
    }
}

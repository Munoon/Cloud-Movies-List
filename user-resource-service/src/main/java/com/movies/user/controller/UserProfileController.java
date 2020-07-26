package com.movies.user.controller;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.User;
import com.movies.common.user.UserMapper;
import com.movies.common.user.UserTo;
import com.movies.user.user.UserService;
import com.movies.user.user.to.UpdateEmailTo;
import com.movies.user.user.to.UpdateProfileTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class UserProfileController {
    private UserService userService;

    @PostMapping("/update")
    public UserTo updateProfile(@Valid @RequestBody UpdateProfileTo updateProfileTo,
                                @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("User {} update profile {}", authorizedUser.getId(), updateProfileTo);
        User user = userService.updateUser(authorizedUser.getId(), updateProfileTo);
        return UserMapper.INSTANCE.asTo(user);
    }

    @PostMapping("/update/email")
    public UserTo updateEmail(@Valid @RequestBody UpdateEmailTo updateEmailTo,
                              @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("User {} update email {}", authorizedUser.getId(), updateEmailTo);
        User user = userService.updateUser(authorizedUser.getId(), updateEmailTo);
        return UserMapper.INSTANCE.asTo(user);
    }
}

package com.movies.user.controller;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.User;
import com.movies.common.user.UserMapper;
import com.movies.common.user.UserTo;
import com.movies.user.user.UserService;
import com.movies.user.user.to.UpdateEmailTo;
import com.movies.user.user.to.UpdatePasswordTo;
import com.movies.user.user.to.UpdateProfileTo;
import com.movies.user.util.UserUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class UserProfileController {
    private UserService userService;

    @GetMapping
    public UserTo getProfile(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("Get profile of user with id {}", authorizedUser.getId());
        return authorizedUser.getUserTo();
    }

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

    @PostMapping("/update/password")
    public UserTo updatePassword(@Valid @RequestBody UpdatePasswordTo updatePasswordTo,
                                 @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("User {} update password", authorizedUser.getId());
        User user = userService.updateUser(authorizedUser.getId(), updatePasswordTo);
        return UserMapper.INSTANCE.asTo(user);
    }

    @DeleteMapping
    public void deleteProfile(@RequestBody Map<String, Object> data,
                              @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        UserUtils.validatePassword(data, authorizedUser.getId(), userService);
        log.info("User {} deleted his profile", authorizedUser.getId());
        userService.deleteUserById(authorizedUser.getId());
    }
}

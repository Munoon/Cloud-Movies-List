package com.movies.user.validators.password;

import com.movies.common.user.User;
import com.movies.user.user.UserService;
import com.movies.user.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class PasswordValidator implements ConstraintValidator<ValidatePassword, String> {
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            return false;
        }

        int userId = SecurityUtils.authUserId();
        User user = userService.getById(userId);
        return passwordEncoder.matches(password, user.getPassword());
    }
}

package com.movies.user.validators.password;

import com.movies.common.AuthorizedUser;
import com.movies.user.AbstractTest;
import com.movies.user.util.TestUtils;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

import static com.movies.user.user.UserTestData.DEFAULT_USER;
import static org.assertj.core.api.Assertions.assertThat;

class PasswordValidatorTest extends AbstractTest {
    @Autowired
    private Validator validator;

    @BeforeEach
    void authorizeUser() {
        SecurityContextHolder.getContext().setAuthentication(new OAuth2Authentication(
                TestUtils.DEFAULT_OAUTH_REQUEST,
                new UsernamePasswordAuthenticationToken(
                        new AuthorizedUser(DEFAULT_USER), null, DEFAULT_USER.getRoles()
                )
        ));
    }

    @Test
    void testValid() {
        var object = new ValidationObject("pass");
        Set<ConstraintViolation<ValidationObject>> validate = validator.validate(object);
        assertThat(validate).hasSize(0);
    }

    @Test
    void testNotValid() {
        var object = new ValidationObject("unCorrectPassword");
        Set<ConstraintViolation<ValidationObject>> validate = validator.validate(object);
        assertThat(validate).hasSize(1);
    }

    @Test
    void testNull() {
        var object = new ValidationObject(null);
        Set<ConstraintViolation<ValidationObject>> validate = validator.validate(object);
        assertThat(validate).hasSize(1);
    }

    @AllArgsConstructor
    private static class ValidationObject {
        @ValidatePassword
        private String password;
    }
}
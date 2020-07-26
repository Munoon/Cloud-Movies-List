package com.movies.user.util;

import org.springframework.security.crypto.password.PasswordEncoder;

public class TestPasswordEncoder implements PasswordEncoder {
    public static final String PREFIX = "{noop}";
    private static final int PREFIX_LENGTH = PREFIX.length();

    @Override
    public String encode(CharSequence rawPassword) {
        return "{noop}" + rawPassword;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword.substring(PREFIX_LENGTH));
    }
}

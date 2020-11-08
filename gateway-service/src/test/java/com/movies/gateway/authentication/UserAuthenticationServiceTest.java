package com.movies.gateway.authentication;

import com.movies.gateway.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserAuthenticationServiceTest extends AbstractTest {
    @Autowired
    private UserAuthenticationService service;

    @Test
    void logoutByToken() {
        // 'test_token' taken from contract
        assertDoesNotThrow(() -> service.logoutByToken("test_token"));
    }
}
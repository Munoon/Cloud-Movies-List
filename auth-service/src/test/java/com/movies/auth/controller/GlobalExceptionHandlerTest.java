package com.movies.auth.controller;

import com.movies.auth.AbstractTest;
import com.movies.auth.holders.gateway.GatewayServiceHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.DefaultServiceInstance;

import java.util.UUID;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest extends AbstractTest {
    @MockBean
    private GatewayServiceHolder gatewayServiceHolder;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void staticNoHandlerFoundExceptionHandler() throws Exception {
        when(gatewayServiceHolder.getGatewayServiceInstance()).thenReturn(null);

        String result = globalExceptionHandler.noHandlerFoundExceptionHandler();
        assertThat(result).isEqualTo("redirect_error");
    }

    @Test
    void gatewayNoHandlerFoundExceptionHandler() throws Exception {
        var gatewayService = new DefaultServiceInstance(UUID.randomUUID().toString(), "gateway-service", "localhost", 8080, false, emptyMap());
        when(gatewayServiceHolder.getGatewayServiceInstance()).thenReturn(gatewayService);

        String result = globalExceptionHandler.noHandlerFoundExceptionHandler();
        assertThat(result).isEqualTo("redirect:http://localhost:8080/login");
    }
}
package com.movies.auth.controller;

import com.movies.auth.AbstractWebTest;
import com.movies.auth.holders.gateway.GatewayServiceHolder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.DefaultServiceInstance;

import java.util.UUID;

import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RootControllerTest extends AbstractWebTest {
    @MockBean
    private GatewayServiceHolder gatewayServiceHolder;

    @Test
    void staticLogin() throws Exception {
        when(gatewayServiceHolder.getGatewayServiceInstance()).thenReturn(null);

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("static/login"));
    }

    @Test
    void gatewayLogin() throws Exception {
        var gatewayService = new DefaultServiceInstance(UUID.randomUUID().toString(), "gateway-service", "localhost", 8080, false, emptyMap());
        when(gatewayServiceHolder.getGatewayServiceInstance()).thenReturn(gatewayService);

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("gateway/login"))
                .andExpect(model().attribute("gatewayService", "http://localhost:8080"));
    }
}
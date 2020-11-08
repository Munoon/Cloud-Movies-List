package com.movies.auth;

import com.movies.auth.holders.gateway.GatewayServiceHolderImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GatewayServiceHolderImplTest extends AbstractTest {
    @MockBean
    private DiscoveryClient discoveryClient;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private GatewayServiceHolderImpl holder;

    @Test
    void getGatewayNull() {
        when(discoveryClient.getInstances("gateway-service")).thenReturn(emptyList());
        eventPublisher.publishEvent(new HeartbeatEvent(new Object(), null));

        assertThat(holder.getGatewayServiceInstance()).isEqualTo(null);
    }

    @Test
    void getGateway() {
        var gatewayService = new DefaultServiceInstance(UUID.randomUUID().toString(), "gateway-service", "localhost", 8080, false, emptyMap());
        when(discoveryClient.getInstances("gateway-service")).thenReturn(singletonList(gatewayService));
        eventPublisher.publishEvent(new HeartbeatEvent(new Object(), null));

        assertThat(holder.getGatewayServiceInstance()).isEqualToComparingFieldByField(gatewayService);
    }
}
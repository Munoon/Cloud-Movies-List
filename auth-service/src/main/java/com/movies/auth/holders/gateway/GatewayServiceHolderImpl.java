package com.movies.auth.holders.gateway;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayServiceHolderImpl implements GatewayServiceHolder {
    private final DiscoveryClient discoveryClient;

    @Getter
    private ServiceInstance gatewayServiceInstance;

    @PostConstruct
    @EventListener(HeartbeatEvent.class)
    public void onHeartbeatEvent() {
        List<ServiceInstance> instances = discoveryClient.getInstances("gateway-service");
        gatewayServiceInstance = instances.isEmpty() ? null : instances.get(0);
        log.info(gatewayServiceInstance != null ? "Gateway service was found: {}" : "Gateway service wasn't found", gatewayServiceInstance);
    }
}

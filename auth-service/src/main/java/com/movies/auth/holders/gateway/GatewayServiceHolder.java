package com.movies.auth.holders.gateway;

import org.springframework.cloud.client.ServiceInstance;

public interface GatewayServiceHolder {
    ServiceInstance getGatewayServiceInstance();
}

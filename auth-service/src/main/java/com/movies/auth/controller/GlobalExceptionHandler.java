package com.movies.auth.controller;

import com.movies.auth.holders.gateway.GatewayServiceHolder;
import lombok.AllArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final GatewayServiceHolder gatewayServiceHolder;

    @ExceptionHandler(NoHandlerFoundException.class)
    public String noHandlerFoundExceptionHandler() throws URISyntaxException, MalformedURLException {
        ServiceInstance gatewayServiceInstance = gatewayServiceHolder.getGatewayServiceInstance();
        if (gatewayServiceInstance == null) {
            return "redirect_error";
        }

        URI gatewayServiceUri = gatewayServiceInstance.getUri();
        URL redirectUrl = new URIBuilder(gatewayServiceUri)
                .setPath("/login")
                .build()
                .toURL();

        return "redirect:" + redirectUrl.toString();
    }
}

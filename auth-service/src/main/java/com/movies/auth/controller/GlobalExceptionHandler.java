package com.movies.auth.controller;

import com.movies.auth.holders.gateway.GatewayServiceHolder;
import com.movies.common.AuthorizedUser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final GatewayServiceHolder gatewayServiceHolder;

    @ExceptionHandler(NoHandlerFoundException.class)
    public String noHandlerFoundExceptionHandler() throws URISyntaxException, MalformedURLException {
        ServiceInstance gatewayServiceInstance = gatewayServiceHolder.getGatewayServiceInstance();
        if (gatewayServiceInstance == null) {
            return "static/redirect_error";
        }

        URI gatewayServiceUri = gatewayServiceInstance.getUri();
        URL redirectUrl = new URIBuilder(gatewayServiceUri)
                .setPath("/login")
                .build()
                .toURL();

        return "redirect:" + redirectUrl.toString();
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView globalExceptionHandler(Exception e, HttpServletRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = "[anonymous]";
        if (authentication instanceof UsernamePasswordAuthenticationToken
                && authentication.getPrincipal() instanceof AuthorizedUser) {
            user = ((AuthorizedUser) authentication.getPrincipal()).getId().toString();
        }

        log.error("Unknown exception on request '{}' by user {}", req.getRequestURL(), user, e);
        return getModelAndView(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SneakyThrows
    private ModelAndView getModelAndView(String message, HttpStatus status) {
        Map<String, Object> params = new HashMap<>(Map.of(
                "error_message", message,
                "error_status", status.value()
        ));
        ServiceInstance gatewayServiceInstance = gatewayServiceHolder.getGatewayServiceInstance();
        if (gatewayServiceInstance != null) {
            params.put("gatewayService", gatewayServiceInstance.getUri().toURL().toString());
            return new ModelAndView("gateway/error", params);
        }
        return new ModelAndView("static/error", params);
    }
}

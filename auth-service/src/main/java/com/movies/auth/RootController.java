package com.movies.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Slf4j
@Controller
@ControllerAdvice
@AllArgsConstructor
public class RootController {
    private final DiscoveryClient discoveryClient;
    private ServiceInstance gatewayServiceInstance;

    @PostConstruct
    @EventListener(HeartbeatEvent.class)
    public void findGatewayService() {
        List<ServiceInstance> instances = discoveryClient.getInstances("gateway-service");
        gatewayServiceInstance = instances.isEmpty() ? null : instances.get(0);
        log.info(gatewayServiceInstance != null ? "Gateway service was found: {}" : "Gateway service wasn't found", gatewayServiceInstance);
    }

    @GetMapping("/login")
    public String login(Model model) throws MalformedURLException {
        if (gatewayServiceInstance == null) {
            return "login_static";
        }

        model.addAttribute("gatewayService", gatewayServiceInstance.getUri().toURL().toString());
        return "login";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String noHandlerFoundExceptionHandler() throws URISyntaxException, MalformedURLException {
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

package com.movies.auth.controller;

import com.movies.auth.holders.gateway.GatewayServiceHolder;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;

@Controller
@AllArgsConstructor
public class RootController {
    private final GatewayServiceHolder gatewayServiceHolder;

    @GetMapping("/login")
    public String login(Model model) throws MalformedURLException {
        ServiceInstance gatewayServiceInstance = gatewayServiceHolder.getGatewayServiceInstance();
        if (gatewayServiceInstance == null) {
            return "static/login";
        }

        model.addAttribute("gatewayService", gatewayServiceInstance.getUri().toURL().toString());
        return "gateway/login";
    }
}

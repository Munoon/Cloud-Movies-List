package com.movies.auth.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Data
@Component
@RefreshScope
@ConfigurationProperties("oauth2")
public class OAuth2Clients {
    private List<ClientConfiguration> clients;

    @Data
    public static class ClientConfiguration {
        private String id;
        private String secret;
        private Set<String> redirectUri;
        private Set<String> authorizedGrantTypes;
        private Set<String> scopes;
        private boolean autoApprove;
        private Integer accessTokenValiditySeconds;
        private Integer refreshTokenValiditySeconds;
    }
}

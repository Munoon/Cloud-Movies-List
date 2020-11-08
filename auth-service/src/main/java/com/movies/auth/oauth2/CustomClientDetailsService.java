package com.movies.auth.oauth2;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Service
@RefreshScope
public class CustomClientDetailsService implements ClientDetailsService {
    private final Map<String, ClientDetails> storage;

    public CustomClientDetailsService(OAuth2Clients oAuth2Clients) {
        this.storage = oAuth2Clients.getClients().stream()
                .map(CustomClientDetails::new)
                .collect(toMap(CustomClientDetails::getClientId, c -> c));
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return Optional.ofNullable(storage.get(clientId))
                .orElseThrow(() -> new ClientRegistrationException("Client with id '" + clientId + "' is not found!"));
    }
}

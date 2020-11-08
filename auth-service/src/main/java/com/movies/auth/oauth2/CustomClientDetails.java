package com.movies.auth.oauth2;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class CustomClientDetails implements ClientDetails {
    private final String clientId;
    private final Set<String> resourceIds;
    private final String clientSecret;
    private final Set<String> scope;
    private final Set<String> authorizedGrantTypes;
    private final Set<String> registeredRedirectUri;
    private final Collection<GrantedAuthority> authorities;
    private final Integer accessTokenValiditySeconds;
    private final Integer refreshTokenValiditySeconds;
    private final boolean alwaysAutoApprove;

    public CustomClientDetails(OAuth2Clients.ClientConfiguration c) {
        this(c.getId(), Collections.emptySet(), "{noop}" + c.getSecret(), c.getScopes(), c.getAuthorizedGrantTypes(), c.getRedirectUri(), Collections.emptyList(), c.getAccessTokenValiditySeconds(), c.getRefreshTokenValiditySeconds(), c.isAutoApprove());
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return resourceIds;
    }

    @Override
    public boolean isSecretRequired() {
        return clientSecret != null;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return scope != null;
    }

    @Override
    public Set<String> getScope() {
        return scope;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return alwaysAutoApprove;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Collections.emptyMap();
    }
}

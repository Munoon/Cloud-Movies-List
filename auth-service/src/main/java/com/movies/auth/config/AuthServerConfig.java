package com.movies.auth.config;

import com.movies.common.user.CustomUserAuthenticationConverter;
import com.movies.auth.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    private final UserService userService;
    private final Environment environment;
    private final Resource storeFile;

    public AuthServerConfig(UserService userService, Environment environment, @Value("${jwt.certificate.store.file}") Resource storeFile) {
        this.userService = userService;
        this.environment = environment;
        this.storeFile = storeFile;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(environment.getRequiredProperty("oauth.client.id"))
                .secret(environment.getRequiredProperty("oauth.client.secret"))
                .redirectUris(environment.getRequiredProperty("oauth.client.redirectUri").split(","))
                .authorizedGrantTypes(environment.getRequiredProperty("oauth.client.authorizedGrantTypes").split(","))
                .scopes(environment.getRequiredProperty("oauth.client.scopes").split(","))
                .autoApprove(environment.getRequiredProperty("oauth.client.autoApprove", Boolean.class))
                .accessTokenValiditySeconds(environment.getRequiredProperty("oauth.client.accessTokenValiditySeconds", Integer.class))
                .refreshTokenValiditySeconds(environment.getRequiredProperty("oauth.client.refreshTokenValiditySeconds", Integer.class));
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userService);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(accessTokenConverter());
        converter.setKeyPair(keyPair());
        return converter;
    }

    @Bean
    public AccessTokenConverter accessTokenConverter() {
        var accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter());
        return accessTokenConverter;
    }

    @Bean
    public CustomUserAuthenticationConverter customUserAuthenticationConverter() {
        return new CustomUserAuthenticationConverter();
    }

    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                storeFile,
                environment.getRequiredProperty("jwt.certificate.store.password").toCharArray()
        );
        return keyStoreKeyFactory.getKeyPair(
                environment.getRequiredProperty("jwt.certificate.key.alias"),
                environment.getRequiredProperty("jwt.certificate.key.password").toCharArray()
        );
    }
}

package com.movies.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Environment environment;

    @Value("${jwt.certificate.store.file}")
    private Resource storeFile;

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
                .userDetailsService(userDetailsService);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                storeFile,
                environment.getRequiredProperty("jwt.certificate.store.password").toCharArray()
        );
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(
                environment.getRequiredProperty("jwt.certificate.key.alias"),
                environment.getRequiredProperty("jwt.certificate.key.password").toCharArray()
        );
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        return converter;
    }
}

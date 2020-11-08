package com.movies.auth.config;

import com.movies.auth.authentications.UserAuthenticationService;
import com.movies.auth.user.UserService;
import com.movies.common.AuthorizedUser;
import com.movies.common.user.CustomUserAuthenticationConverter;
import com.movies.common.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.security.KeyPair;

import static java.util.Collections.singletonList;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    private final UserService userService;
    private final Environment environment;
    private final Resource storeFile;
    private final UserAuthenticationService userAuthenticationService;

    public AuthServerConfig(UserService userService, Environment environment, @Value("${jwt.certificate.store.file}") Resource storeFile, UserAuthenticationService userAuthenticationService) {
        this.userService = userService;
        this.environment = environment;
        this.storeFile = storeFile;
        this.userAuthenticationService = userAuthenticationService;
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
        CustomTokenServices tokenServices = new CustomTokenServices(userAuthenticationService);

        endpoints
                .tokenServices(tokenServices)
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userService);

        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(jwtAccessTokenConverter());

        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userService));
        tokenServices.setAuthenticationManager(new ProviderManager(singletonList(provider)));
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
        return new CustomUserAuthenticationConverter() {
            @Override
            public AuthorizedUser getAuthorizedUser(int userId) {
                User user = userService.getById(userId);
                return new AuthorizedUser(user);
            }
        };
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

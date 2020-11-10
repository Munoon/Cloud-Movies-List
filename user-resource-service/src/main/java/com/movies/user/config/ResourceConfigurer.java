package com.movies.user.config;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.CustomUserAuthenticationConverter;
import com.movies.common.user.User;
import com.movies.user.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceConfigurer extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                    .and()
                .authorizeRequests()
                    .antMatchers("/actuator/**", "/microservices/**").permitAll()
                    .antMatchers("/register", "/test/email/*").permitAll()
                    .anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AccessTokenConverter accessTokenConverter(JwtAccessTokenConverter jwtAccessTokenConverter) {
        var accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter(null));
        jwtAccessTokenConverter.setAccessTokenConverter(accessTokenConverter);
        return accessTokenConverter;
    }

    @Bean
    public CustomUserAuthenticationConverter customUserAuthenticationConverter(UserService userService) {
        return new CustomUserAuthenticationConverter() {
            @Override
            public AuthorizedUser getAuthorizedUser(int userId) {
                User user = userService.getById(userId);
                return new AuthorizedUser(user);
            }
        };
    }
}

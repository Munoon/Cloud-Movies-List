package com.movies.gateway.config;

import com.movies.common.AuthorizedUser;
import com.movies.common.util.UserUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Configuration
@EnableOAuth2Sso
@EnableZuulProxy
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(
                        "/", "/static/**",
                        "/users/register", "/users/test/email/*",
                        "/actuator/**", "/movie/**", "/movies/**"
                ).permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/users/actuator/**", "/users/microservices/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Bean
    @LoadBalanced
    public OAuth2RestOperations restOperations(OAuth2ProtectedResourceDetails resource,
                                               @Qualifier("oauth2ClientContext") OAuth2ClientContext context,
                                               UserInfoTokenServices userInfoTokenServices) {
        var restTemplate = new OAuth2RestTemplate(resource, context);
        userInfoTokenServices.setRestTemplate(restTemplate);
        return restTemplate;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public PrincipalExtractor authorizedUserPrincipalExtractor() {
        return AuthorizedUser::new;
    }

    @Bean
    @Primary
    public AuthoritiesExtractor customAuthoritiesExtractor() {
        return map -> UserUtils.getUserRoles(map)
                .stream()
                .map(it -> (GrantedAuthority) it)
                .collect(Collectors.toList());
    }
}

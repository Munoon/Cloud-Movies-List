package com.movies.gateway;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.Map;

@Configuration
@EnableOAuth2Sso
@EnableZuulProxy
@EnableRedisHttpSession
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/static/**", "/user-resource-service/register").permitAll()
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
    public OAuth2RestOperations restOperations(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        return new OAuth2RestTemplate(resource, context);
    }

    @Bean
    @Primary
    public UserInfoTokenServices myUserInfoTokenServices(ResourceServerProperties properties) {
        return new UserInfoTokenServices(properties.getUserInfoUri(), properties.getClientId()) {
            @Override
            protected Object getPrincipal(Map<String, Object> map) {
                return map.get("email");
            }
        };
    }
}

package com.movies.list.config

import com.movies.common.AuthorizedUser
import com.movies.common.user.CustomUserAuthenticationConverter
import com.movies.common.user.User
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.provider.token.AccessTokenConverter
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.web.client.RestTemplate

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ResourceConfigurer : ResourceServerConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http!!.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
            .authorizeRequests()
                .antMatchers("/templates/**").permitAll()
                .antMatchers("/graphql/**", "/movie/**").permitAll()
                .anyRequest().denyAll()
    }

    @Bean
    fun accessTokenConverter(jwtAccessTokenConverter: JwtAccessTokenConverter): AccessTokenConverter {
        val accessTokenConverter = DefaultAccessTokenConverter()
        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter(null))
        jwtAccessTokenConverter.accessTokenConverter = accessTokenConverter
        return accessTokenConverter
    }

    @Bean
    fun customUserAuthenticationConverter(restTemplate: RestTemplate?): CustomUserAuthenticationConverter? {
        return object : CustomUserAuthenticationConverter() {
            override fun getAuthorizedUser(userId: Int): AuthorizedUser {
                val url = "http://user-resource-service/microservices/info/$userId"
                val user = restTemplate!!.getForObject(url, User::class.java)!!
                user.password = user.password ?: "N/A"
                return AuthorizedUser(user)
            }
        }
    }

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
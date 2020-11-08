package com.movies.auth.config;

import com.movies.auth.authentications.UserAuthenticationService;
import com.movies.auth.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login**").anonymous()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .csrf().and()
            .logout().disable()
            .formLogin()
                .loginPage("/login")
                .and()
            .addFilterAfter(userAuthenticationValidationFilter(null), UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    public UserAuthenticationValidationFilter userAuthenticationValidationFilter(UserAuthenticationService service) {
        return new UserAuthenticationValidationFilter(service);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }
}

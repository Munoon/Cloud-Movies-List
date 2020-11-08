package com.movies.user.controller;

import com.movies.user.authentications.UserAuthenticationEntity;
import com.movies.user.authentications.UserAuthenticationRepository;
import com.movies.user.authentications.UserAuthenticationJwtEntity;
import com.movies.user.authentications.UserAuthenticationJwtRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Date;

import static com.movies.user.util.JsonUtil.writeValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TokenValidationControllerTest extends AbstractWebTest {
    @Autowired
    private UserAuthenticationRepository repository;

    @Autowired
    private UserAuthenticationJwtRepository jwtRepository;

    @Test
    void invalidateToken() throws Exception {
        final String token = "token";
        UserAuthenticationEntity userAuthentication = repository.save(new UserAuthenticationEntity("session"));
        jwtRepository.save(new UserAuthenticationJwtEntity(token, new Date(), userAuthentication));

        assertThat(repository.count()).isEqualTo(1);
        assertThat(jwtRepository.count()).isEqualTo(1);

        mockMvc.perform(post("/microservices/token/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(token)))
                .andExpect(status().isNoContent());

        assertThat(repository.count()).isEqualTo(0);
        assertThat(jwtRepository.count()).isEqualTo(0);
    }
}
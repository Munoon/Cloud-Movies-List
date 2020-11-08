package com.movies.gateway.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.gateway.utils.exception.RequestException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAuthenticationService {
    private ObjectMapper mapper;
    private UserAuthenticationClient client;

    public void logoutByToken(String token) {
        try {
            String json = mapper.writeValueAsString(token);
            client.logoutByToken(json);
        } catch (FeignException e) {
            if (e.status() == -1) {
                throw e;
            }

            throw new RequestException("Error invalidate user token", HttpStatus.INTERNAL_SERVER_ERROR, e);
        } catch (JsonProcessingException e) {
            throw new RequestException("Error invalidate user token", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}

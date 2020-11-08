package com.movies.user.authentications;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAuthenticationService {
    private UserAuthenticationRepository repository;

    public void invalidateToken(String token) {
        repository.deleteByToken(token);
    }
}

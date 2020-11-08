package com.movies.auth.authentications;

import com.movies.auth.authentications.jwt.UserAuthenticationJwtEntity;
import com.movies.auth.authentications.jwt.UserAuthenticationJwtRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
@AllArgsConstructor
public class UserAuthenticationService {
    private UserAuthenticationRepository repository;
    private UserAuthenticationJwtRepository jwtRepository;

    public UserAuthenticationEntity create(UserAuthenticationEntity entity) {
        Assert.notNull(entity, "User authentication entity must be not null");
        Assert.isNull(entity.getId(), "User authentication entity must be not null");
        return repository.save(entity);
    }

    public UserAuthenticationJwtEntity create(UserAuthenticationJwtEntity entity) {
        Assert.notNull(entity, "User authentication jwt entity must be not null");
        Assert.isNull(entity.getId(), "User authentication jwt entity must be not null");

        UserAuthenticationEntity authentication = entity.getAuthentication();
        UserAuthenticationEntity authenticationEntity = repository.findBySessionId(authentication.getSessionId()).orElse(null);
        if (authenticationEntity == null) {
            authenticationEntity = create(authentication);
        }
        entity.setAuthentication(authenticationEntity);

        return jwtRepository.save(entity);
    }

    public boolean testSession(String sessionId) {
        Assert.notNull(sessionId, "Session id must be not null");
        return repository.countAllBySessionId(sessionId) > 0;
    }

    @Scheduled(cron = "${authentications.clean.cron}")
    public void removeExpiredTokens() {
        log.info("Removing expired authentication token");
        jwtRepository.removeExpiredTokens();
    }
}

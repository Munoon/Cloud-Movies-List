package com.movies.auth.authentications;

import com.movies.auth.AbstractTest;
import com.movies.auth.authentications.jwt.UserAuthenticationJwtEntity;
import com.movies.auth.authentications.jwt.UserAuthenticationJwtRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

class UserAuthenticationServiceTest extends AbstractTest {
    @Autowired
    private UserAuthenticationService service;

    @Autowired
    private UserAuthenticationRepository repository;

    @Autowired
    private UserAuthenticationJwtRepository jwtRepository;

    @Test
    void create() {
        UserAuthenticationEntity create = new UserAuthenticationEntity("session");
        UserAuthenticationEntity created = service.create(create);

        UserAuthenticationEntity expected = new UserAuthenticationEntity(created.getId(), "session");
        assertThat(repository.findAll()).usingFieldByFieldElementComparator().isEqualTo(singleton(expected));
    }

    @Test
    void createJwt() {
        UserAuthenticationEntity userAuthentication = new UserAuthenticationEntity("session");
        Date date = new Date();
        UserAuthenticationJwtEntity create = new UserAuthenticationJwtEntity("token", date, userAuthentication);
        UserAuthenticationJwtEntity created = service.create(create);

        UserAuthenticationEntity userAuthenticationExpected = new UserAuthenticationEntity(created.getAuthentication().getId(), "session");
        UserAuthenticationJwtEntity expected = new UserAuthenticationJwtEntity(created.getId(), "token", date, userAuthenticationExpected);
        assertThat(jwtRepository.findAll()).usingElementComparatorIgnoringFields("expiration").isEqualTo(singleton(expected));
    }

    @Test
    void createJwtSessionCreated() {
        UserAuthenticationEntity userAuthentication = service.create(new UserAuthenticationEntity("session"));

        Date date = new Date();
        UserAuthenticationJwtEntity create = new UserAuthenticationJwtEntity("token", date, userAuthentication);
        UserAuthenticationJwtEntity created = service.create(create);

        UserAuthenticationEntity userAuthenticationExpected = new UserAuthenticationEntity(userAuthentication.getId(), "session");
        UserAuthenticationJwtEntity expected = new UserAuthenticationJwtEntity(created.getId(), "token", date, userAuthenticationExpected);
        assertThat(jwtRepository.findAll()).usingElementComparatorIgnoringFields("expiration").isEqualTo(singleton(expected));
    }

    @Test
    void testSession() {
        service.create(new UserAuthenticationEntity("session"));

        assertThat(service.testSession("session")).isEqualTo(true);
        assertThat(service.testSession("unknown_session")).isEqualTo(false);
    }

    @Test
    void removeExpiredTokens() {
        UserAuthenticationJwtEntity created = service.create(new UserAuthenticationJwtEntity(
                "token",
                Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC)),
                new UserAuthenticationEntity("session")
        ));
        service.create(new UserAuthenticationJwtEntity(
                "token2",
                Date.from(LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC)),
                new UserAuthenticationEntity("session2")
        ));
        assertThat(repository.count()).isEqualTo(2);
        assertThat(jwtRepository.count()).isEqualTo(2);

        service.removeExpiredTokens();

        UserAuthenticationJwtEntity expected = new UserAuthenticationJwtEntity(
                created.getId(),
                "token",
                created.getExpiration(),
                new UserAuthenticationEntity(created.getAuthentication().getId(), "session")
        );

        assertThat(repository.count()).isEqualTo(2);
        assertThat(jwtRepository.count()).isEqualTo(1);
        assertThat(jwtRepository.findAll()).usingElementComparatorIgnoringFields("expiration").isEqualTo(singleton(expected));
    }
}
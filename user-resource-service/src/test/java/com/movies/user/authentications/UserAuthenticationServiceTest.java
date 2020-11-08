package com.movies.user.authentications;

import com.movies.user.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

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
    void invalidateToken() {
        final String token = "token";
        UserAuthenticationEntity userAuthentication = repository.save(new UserAuthenticationEntity("session"));
        jwtRepository.save(new UserAuthenticationJwtEntity(token, new Date(), userAuthentication));

        UserAuthenticationEntity userAuthentication2 = repository.save(new UserAuthenticationEntity("session2"));
        var createdToken1 = jwtRepository.save(new UserAuthenticationJwtEntity("token2", new Date(), userAuthentication2));
        var createdToken2 = jwtRepository.save(new UserAuthenticationJwtEntity("token3", new Date(), userAuthentication2));

        assertThat(repository.count()).isEqualTo(2);
        assertThat(jwtRepository.count()).isEqualTo(3);

        service.invalidateToken(token);

        assertThat(repository.count()).isEqualTo(1);
        assertThat(jwtRepository.count()).isEqualTo(2);

        UserAuthenticationEntity expected = new UserAuthenticationEntity(userAuthentication2.getId(), "session2");
        assertThat(repository.findAll()).usingFieldByFieldElementComparator().isEqualTo(singleton(expected));
        assertThat(jwtRepository.findAll()).usingElementComparatorIgnoringFields("expiration").isEqualTo(List.of(
                new UserAuthenticationJwtEntity(createdToken1.getId(), "token2", createdToken1.getExpiration(), expected),
                new UserAuthenticationJwtEntity(createdToken2.getId(), "token3", createdToken2.getExpiration(), expected)
        ));
    }
}
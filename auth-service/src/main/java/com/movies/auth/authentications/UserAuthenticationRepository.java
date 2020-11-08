package com.movies.auth.authentications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserAuthenticationRepository extends JpaRepository<UserAuthenticationEntity, Integer> {
    int countAllBySessionId(String sessionId);

    Optional<UserAuthenticationEntity> findBySessionId(String sessionId);
}

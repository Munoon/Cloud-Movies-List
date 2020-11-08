package com.movies.auth.authentications.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserAuthenticationJwtRepository extends JpaRepository<UserAuthenticationJwtEntity, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserAuthenticationJwtEntity e WHERE e.expiration < CURRENT_TIMESTAMP")
    void removeExpiredTokens();
}

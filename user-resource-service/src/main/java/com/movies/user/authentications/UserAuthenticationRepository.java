package com.movies.user.authentications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserAuthenticationRepository extends JpaRepository<UserAuthenticationEntity, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserAuthenticationEntity WHERE id = (SELECT j.id FROM UserAuthenticationJwtEntity j WHERE j.token = ?1)")
    void deleteByToken(String token);
}

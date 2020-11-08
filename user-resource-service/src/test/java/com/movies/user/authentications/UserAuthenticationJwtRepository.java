package com.movies.user.authentications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserAuthenticationJwtRepository extends JpaRepository<UserAuthenticationJwtEntity, Integer> {
}

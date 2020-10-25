package com.movies.user.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);

    int countAllByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserEntity WHERE id = ?1")
    int delete(Integer id);
}

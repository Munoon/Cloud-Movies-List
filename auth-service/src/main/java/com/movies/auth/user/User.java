package com.movies.auth.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class User {
    private Integer id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private LocalDateTime registered;

    private Set<UserRole> roles;
}

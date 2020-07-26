package com.movies.common.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class User implements Serializable {
    private Integer id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private LocalDateTime registered;

    private Set<UserRoles> roles;

    public User(User u) {
        this(u.getId(), u.getName(), u.getSurname(), u.getEmail(), u.getPassword(), u.getRegistered(), u.getRoles());
    }
}

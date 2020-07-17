package com.movies.common.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserTo implements Serializable {
    private Integer id;

    private String name;

    private String surname;

    private String email;

    private Set<UserRoles> roles;
}

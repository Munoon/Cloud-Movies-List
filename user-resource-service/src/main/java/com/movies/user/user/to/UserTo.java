package com.movies.user.user.to;

import com.movies.user.user.UserRoles;
import lombok.Data;

import java.util.Set;

@Data
public class UserTo {
    private Integer id;

    private String name;

    private String surname;

    private String email;

    private Set<UserRoles> roles;
}

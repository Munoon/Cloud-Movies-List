package com.movies.common.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTo implements Serializable {
    private Integer id;

    private String name;

    private String surname;

    private String email;

    private Set<UserRoles> roles;
}

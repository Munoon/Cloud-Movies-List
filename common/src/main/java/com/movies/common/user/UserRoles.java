package com.movies.common.user;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public enum UserRoles implements GrantedAuthority, Serializable {
    ROLE_USER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}

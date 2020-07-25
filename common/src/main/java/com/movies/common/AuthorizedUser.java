package com.movies.common;

import com.movies.common.user.User;
import com.movies.common.user.UserMapper;
import com.movies.common.user.UserTo;
import com.movies.common.util.UserUtils;
import lombok.Getter;

import java.util.Map;

@Getter
public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private final UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.userTo = UserMapper.INSTANCE.asTo(user);
    }

    public AuthorizedUser(UserTo userTo) {
        super(userTo.getEmail(), "N/A", userTo.getRoles());
        this.userTo = userTo;
    }

    public AuthorizedUser(Map<String, Object> params) {
        super((String) params.get("email"), UserUtils.getPasswordFromMap(params), UserUtils.getUserRoles(params));
        this.userTo = UserMapper.INSTANCE.asTo(params);
    }

    public Integer getId() {
        return userTo.getId();
    }
}

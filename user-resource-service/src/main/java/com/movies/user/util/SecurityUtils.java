package com.movies.user.util;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.UserTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.requireNonNull;

public class SecurityUtils {
    private static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return null;
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser getAuthorizedUser() {
        AuthorizedUser user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public static UserTo authUser() {
        return getAuthorizedUser().getUserTo();
    }

    public static int authUserId() {
        return getAuthorizedUser().getId();
    }
}

package com.movies.common.user;

import com.movies.common.AuthorizedUser;

public class EmptyCustomUserAuthenticationConverterImpl extends CustomUserAuthenticationConverter {
    @Override
    public AuthorizedUser getAuthorizedUser(int userId) {
        return null;
    }
}

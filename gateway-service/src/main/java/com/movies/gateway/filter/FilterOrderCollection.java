package com.movies.gateway.filter;

public class FilterOrderCollection {
    /**
     * Order of filter, that update user details after each request, that updating user information.
     *
     * @see UpdateUserDetailsFilter
     */
    public static final int UPDATE_USER_DETAILS_FILTER_ORDER = 1;

    /**
     * Order of filter, that logout user after request, that update user's email or password.
     *
     * @see LogoutUserFilter
     */
    public static final int LOGOUT_USER_FILTER_ORDER = 0;
}

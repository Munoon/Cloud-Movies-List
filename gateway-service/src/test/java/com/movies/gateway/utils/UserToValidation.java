package com.movies.gateway.utils;

import com.movies.common.user.UserTo;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.assertThat;

public class UserToValidation {
    public static ResultMatcher userToResponse(UserTo expected) {
        return mvcResult -> {
            UserTo userTo = JsonUtil.readFromJson(mvcResult, UserTo.class);
            assertThat(userTo).isEqualToComparingFieldByField(expected);
        };
    }
}

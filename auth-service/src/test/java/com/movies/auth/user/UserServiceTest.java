package com.movies.auth.user;

import com.movies.auth.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.movies.auth.user.UserTestData.DEFAULT_USER;
import static com.movies.auth.user.UserTestData.DEFAULT_USER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest extends AbstractTest {
    @Autowired
    private UserService userService;

    @Test
    void loadUserByUsername() {
        UserDetails userDetails = userService.loadUserByUsername(DEFAULT_USER_EMAIL);

        assertThat(userDetails.getUsername()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(userDetails.getPassword()).isEqualTo(DEFAULT_USER.getPassword());
        assertThat(userDetails.getAuthorities()).usingDefaultElementComparator().isEqualTo(DEFAULT_USER.getRoles());
    }

    @Test
    void loadUserByUsernameNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("unknownEmail@email.com"));
    }
}
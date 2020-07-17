package com.movies.auth.user;

import com.movies.auth.AbstractTest;
import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static java.util.Collections.unmodifiableSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserServiceTest extends AbstractTest {
    @Autowired
    private UserService userService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void loadUserByUsername() {
        User result = new User();
        result.setEmail("email@example.com");
        result.setPassword("test");
        result.setRoles(Collections.singleton(UserRoles.ROLE_USER));

        when(restTemplate.getForEntity("http://user-resource-service/login/email@example.com", User.class))
                .thenReturn(new ResponseEntity<>(result, HttpStatus.OK));

        UserDetails userDetails = userService.loadUserByUsername("email@example.com");
        assertThat(userDetails.getUsername()).isEqualTo("email@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("test");
        assertThat(userDetails.getAuthorities())
                .usingDefaultElementComparator()
                .isEqualTo(unmodifiableSet(Collections.singleton(UserRoles.ROLE_USER)));
    }

    @Test
    void loadUserByUsernameNotFound() {
        when(restTemplate.getForEntity("http://user-resource-service/login/email@example.com", User.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("email@example.com"));
    }
}
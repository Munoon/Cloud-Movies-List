package com.movies.auth.user;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<User> response;
        try {
            response = restTemplate.getForEntity(
                    "http://user-resource-service/login/" + username,
                    User.class
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error getting user with username " + username, e);
        }

        User user = response.getBody();
        if (!response.getStatusCode().is2xxSuccessful() || user == null) {
            throw new UsernameNotFoundException("Error getting user with username " + username);
        }

        return new AuthorizedUser(user);
    }
}

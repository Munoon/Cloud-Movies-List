package com.movies.auth.user;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .stream()
                .map(LocalUserMapper.INSTANCE::asUser)
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));

        return new AuthorizedUser(user);
    }
}

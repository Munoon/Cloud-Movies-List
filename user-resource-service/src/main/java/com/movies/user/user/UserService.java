package com.movies.user.user;

import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.util.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterUserTo registerUserTo) {
        String password = passwordEncoder.encode(registerUserTo.getPassword());
        User user = new User( // TODO refactor it with library
                null,
                registerUserTo.getName(), registerUserTo.getSurname(),
                registerUserTo.getEmail(), password,
                LocalDateTime.now(),
                Collections.singleton(UserRoles.ROLE_USER)
        );
        return userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " is not found!"));
    }
}

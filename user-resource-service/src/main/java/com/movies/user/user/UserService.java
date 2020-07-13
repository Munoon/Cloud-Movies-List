package com.movies.user.user;

import com.movies.user.user.to.RegisterUserTo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterUserTo registerUserTo) {
        String password = passwordEncoder.encode(registerUserTo.getPassword());
        User user = new User(
                null,
                registerUserTo.getName(), registerUserTo.getSurname(),
                registerUserTo.getEmail(), password,
                LocalDateTime.now()
        );
        return userRepository.save(user);
    }
}

package com.movies.user.user;

import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.util.exception.NotFoundException;
import com.movies.user.util.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterUserTo registerUserTo) {
        String password = passwordEncoder.encode(registerUserTo.getPassword());
        User user = UserMapper.INSTANCE.toUser(registerUserTo, password);
        return userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " is not found!"));
    }
}

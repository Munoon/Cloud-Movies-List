package com.movies.user.user;

import com.movies.common.user.User;
import com.movies.user.user.to.RegisterUserTo;
import com.movies.user.util.exception.NotFoundException;
import com.movies.common.user.UserMapper;
import com.movies.user.util.mapper.LocalUserMapper;
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
        UserEntity user = LocalUserMapper.INSTANCE.toUserEntity(registerUserTo, password);
        userRepository.save(user);
        return LocalUserMapper.INSTANCE.asUser(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(LocalUserMapper.INSTANCE::asUser)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " is not found!"));
    }
}

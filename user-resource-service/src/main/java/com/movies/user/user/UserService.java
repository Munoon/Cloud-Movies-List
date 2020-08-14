package com.movies.user.user;

import com.movies.common.user.User;
import com.movies.user.user.to.*;
import com.movies.user.util.exception.NotFoundException;
import com.movies.user.util.mapper.LocalUserMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User createUser(RegisterUserTo registerUserTo) {
        UserEntity user = LocalUserMapper.INSTANCE.toUserEntity(registerUserTo, passwordEncoder);
        userRepository.save(user);
        return LocalUserMapper.INSTANCE.asUser(user);
    }

    public User createUser(AdminCreateUserTo adminCreateUserTo) {
        UserEntity user = LocalUserMapper.INSTANCE.toUserEntity(adminCreateUserTo, passwordEncoder);
        userRepository.save(user);
        return LocalUserMapper.INSTANCE.asUser(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(LocalUserMapper.INSTANCE::asUser)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " is not found!"));
    }

    public User getById(int id) {
        UserEntity userEntity = getUserEntityById(id);
        return LocalUserMapper.INSTANCE.asUser(userEntity);
    }

    public User updateUser(int id, UpdateProfileTo updateProfileTo) {
        UserEntity userEntity = getUserEntityById(id);
        LocalUserMapper.INSTANCE.updateEntity(updateProfileTo, userEntity);
        UserEntity updated = userRepository.save(userEntity);
        return LocalUserMapper.INSTANCE.asUser(updated);
    }

    public User updateUser(int id, UpdateEmailTo updateEmailTo) {
        UserEntity userEntity = getUserEntityById(id);
        LocalUserMapper.INSTANCE.updateEntity(updateEmailTo, userEntity);
        UserEntity updated = userRepository.save(userEntity);
        return LocalUserMapper.INSTANCE.asUser(updated);
    }

    public User updateUser(int id, UpdatePasswordTo updatePasswordTo) {
        UserEntity userEntity = getUserEntityById(id);
        LocalUserMapper.INSTANCE.updateEntity(updatePasswordTo, userEntity, passwordEncoder);
        UserEntity updated = userRepository.save(userEntity);
        return LocalUserMapper.INSTANCE.asUser(updated);
    }

    public User updateUser(int id, AdminUpdateUserTo userTo) {
        UserEntity userEntity = getUserEntityById(id);
        LocalUserMapper.INSTANCE.updateEntity(userTo, userEntity);
        UserEntity updated = userRepository.save(userEntity);
        return LocalUserMapper.INSTANCE.asUser(updated);
    }

    private UserEntity getUserEntityById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " is not found!"));
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(LocalUserMapper.INSTANCE::asUser);
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    public boolean testEmail(String email) {
        return userRepository.countAllByEmail(email.toLowerCase()) == 0;
    }

    public boolean testPassword(int userId, String password) {
        User user = getById(userId);
        return passwordEncoder.matches(password, user.getPassword());
    }
}

package com.movies.user.user;

import com.movies.common.user.User;
import com.movies.user.config.MetricsConfig;
import com.movies.user.user.to.*;
import com.movies.user.util.exception.NotFoundException;
import com.movies.user.util.mapper.LocalUserMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UserService {
    private CacheManager cacheManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private Counter registeredUsersCounter;
    private Counter deletedUsersCounter;

    public UserService(CacheManager cacheManager, UserRepository userRepository, PasswordEncoder passwordEncoder, MeterRegistry registry) {
        this.cacheManager = cacheManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.registeredUsersCounter = registry.counter(MetricsConfig.USERS_REGISTERED_COUNTER_NAME);
        this.deletedUsersCounter = registry.counter(MetricsConfig.USERS_DELETED_COUNTER_NAME);
    }

    @Caching(
            put = {
                @CachePut(value = "user", key = "#result.id"),
                @CachePut(value = "user_email", key = "#result.email")
            },
            evict = {
                @CacheEvict(value = "users_list", allEntries = true),
                @CacheEvict(value = "users_count", allEntries = true)
            }
    )
    public User createUser(RegisterUserTo registerUserTo) {
        UserEntity user = LocalUserMapper.INSTANCE.toUserEntity(registerUserTo, passwordEncoder);
        userRepository.save(user);
        registeredUsersCounter.increment();
        return LocalUserMapper.INSTANCE.asUser(user);
    }

    @Caching(
            put = {
                @CachePut(value = "user", key = "#result.id"),
                @CachePut(value = "user_email", key = "#result.email")
            },
            evict = {
                @CacheEvict(value = "users_list", allEntries = true),
                @CacheEvict(value = "users_count", allEntries = true)
            }
    )
    public User createUser(AdminCreateUserTo adminCreateUserTo) {
        UserEntity user = LocalUserMapper.INSTANCE.toUserEntity(adminCreateUserTo, passwordEncoder);
        userRepository.save(user);
        return LocalUserMapper.INSTANCE.asUser(user);
    }

    @Cacheable(value = "user_email", key = "#email")
    public User getByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .map(LocalUserMapper.INSTANCE::asUser)
                .orElseThrow(() -> new NotFoundException("User with email '" + email + "' is not found!"));
    }

    @Cacheable(value = "user", key = "#id")
    public User getById(int id) {
        UserEntity userEntity = getUserEntityById(id);
        return LocalUserMapper.INSTANCE.asUser(userEntity);
    }

    @Caching(
            put = {
                @CachePut(value = "user", key = "#id"),
                @CachePut(value = "user_email", key = "#result.email")
            },
            evict = @CacheEvict(value = "users_list", allEntries = true)
    )
    public User updateUser(int id, UpdateProfileTo updateProfileTo) {
        UserEntity userEntity = getUserEntityById(id);
        LocalUserMapper.INSTANCE.updateEntity(updateProfileTo, userEntity);
        UserEntity updated = userRepository.save(userEntity);
        return LocalUserMapper.INSTANCE.asUser(updated);
    }

    @Caching(
            put = {
                @CachePut(value = "user", key = "#id"),
                @CachePut(value = "user_email", key = "#result.email")
            },
            evict = @CacheEvict(value = "users_list", allEntries = true)
    )
    public User updateUser(int id, UpdateEmailTo updateEmailTo) {
        UserEntity userEntity = getUserEntityById(id);
        userEmailCache(c -> c.evict(userEntity.getEmail()));
        LocalUserMapper.INSTANCE.updateEntity(updateEmailTo, userEntity);
        UserEntity updated = userRepository.save(userEntity);
        return LocalUserMapper.INSTANCE.asUser(updated);
    }

    @Caching(
            put = {
                @CachePut(value = "user", key = "#id"),
                @CachePut(value = "user_email", key = "#result.email")
            },
            evict = @CacheEvict(value = "users_list", allEntries = true)
    )
    public User updateUser(int id, UpdatePasswordTo updatePasswordTo) {
        UserEntity userEntity = getUserEntityById(id);
        LocalUserMapper.INSTANCE.updateEntity(updatePasswordTo, userEntity, passwordEncoder);
        UserEntity updated = userRepository.save(userEntity);
        return LocalUserMapper.INSTANCE.asUser(updated);
    }

    @Caching(
            put = {
                @CachePut(value = "user", key = "#id"),
                @CachePut(value = "user_email", key = "#result.email")
            },
            evict = @CacheEvict(value = "users_list", allEntries = true)
    )
    public User updateUser(int id, AdminUpdateUserTo userTo) {
        UserEntity userEntity = getUserEntityById(id);
        userEmailCache(c -> c.evict(userEntity.getEmail()));
        LocalUserMapper.INSTANCE.updateEntity(userTo, userEntity);
        UserEntity updated = userRepository.save(userEntity);
        return LocalUserMapper.INSTANCE.asUser(updated);
    }

    private UserEntity getUserEntityById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id '" + id + "' is not found!"));
    }

    @Cacheable("users_list")
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(LocalUserMapper.INSTANCE::asUser);
    }

    @Caching(evict = {
            @CacheEvict(value = "user", key = "#id"),
            @CacheEvict(value = "users_list", allEntries = true),
            @CacheEvict(value = "users_count", allEntries = true)
    })
    public void deleteUserById(int id) {
        UserEntity userEntity = getUserEntityById(id);
        userEmailCache(c -> c.evict(userEntity.getEmail()));
        userRepository.delete(id);
        deletedUsersCounter.increment();
    }

    public boolean testEmail(String email) {
        return userRepository.countAllByEmail(email.toLowerCase()) == 0;
    }

    public boolean testPassword(int userId, String password) {
        User user = getById(userId);
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Cacheable("users_count")
    public long count() {
        return userRepository.count();
    }

    private void userEmailCache(Consumer<Cache> modifier) {
        Cache cache = cacheManager.getCache("user_email");
        if (cache != null) {
            modifier.accept(cache);
        }
    }
}

package com.movies.user.util.mapper;

import com.movies.common.user.User;
import com.movies.common.user.UserRoles;
import com.movies.common.user.UserTo;
import com.movies.user.user.UserEntity;
import com.movies.user.user.to.*;
import com.movies.user.util.TestPasswordEncoder;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    @Test
    void toUserEntity() {
        RegisterUserTo registerUserTo = new RegisterUserTo();
        registerUserTo.setEmail("TestEmail");
        registerUserTo.setName("New");
        registerUserTo.setSurname("User");
        registerUserTo.setPassword("newPassword");

        UserEntity user = LocalUserMapper.INSTANCE.toUserEntity(registerUserTo, new TestPasswordEncoder());
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("New");
        assertThat(user.getSurname()).isEqualTo("User");
        assertThat(user.getEmail()).isEqualTo("testemail");
        assertThat(user.getPassword()).isEqualTo(TestPasswordEncoder.PREFIX + "newPassword");
        assertThat(user.getRegistered()).isNotNull();
        assertThat(user.getRoles()).usingDefaultComparator().isEqualTo(Collections.singleton(UserRoles.ROLE_USER));
    }

    @Test
    void toUserEntityFromAdminRegisterUserTo() {
        AdminCreateUserTo adminCreateUserTo = new AdminCreateUserTo();
        adminCreateUserTo.setName("New");
        adminCreateUserTo.setSurname("User");
        adminCreateUserTo.setEmail("Test@example.com");
        adminCreateUserTo.setPassword("examplePassword");
        adminCreateUserTo.setRoles(Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));

        UserEntity user = LocalUserMapper.INSTANCE.toUserEntity(adminCreateUserTo, new TestPasswordEncoder());
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("New");
        assertThat(user.getSurname()).isEqualTo("User");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo(TestPasswordEncoder.PREFIX + "examplePassword");
        assertThat(user.getRegistered()).isNotNull();
        assertThat(user.getRoles()).usingDefaultComparator().isEqualTo(Set.of(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN));
    }

    @Test
    void asUser() {
        UserEntity userEntity = new UserEntity(100, "Test", "User", "email@example.com", "password", LocalDateTime.of(2019, 12, 27, 13, 0), Collections.singleton(UserRoles.ROLE_USER));
        User user = LocalUserMapper.INSTANCE.asUser(userEntity);
        assertThat(user.getId()).isEqualTo(100);
        assertThat(user.getName()).isEqualTo("Test");
        assertThat(user.getSurname()).isEqualTo("User");
        assertThat(user.getEmail()).isEqualTo("email@example.com");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getRegistered()).isEqualTo(LocalDateTime.of(2019, 12, 27, 13, 0));
        assertThat(user.getRoles()).isEqualTo(Collections.singleton(UserRoles.ROLE_USER));
    }

    @Test
    void updateEntityProfile() {
        UpdateProfileTo updateProfileTo = new UpdateProfileTo();
        updateProfileTo.setName("NewName");
        updateProfileTo.setSurname("NewSurname");

        UserEntity userEntity = new UserEntity(100, "Test", "User", "email@example.com", "password", LocalDateTime.of(2019, 12, 27, 13, 0), Collections.singleton(UserRoles.ROLE_USER));
        LocalUserMapper.INSTANCE.updateEntity(updateProfileTo, userEntity);

        assertThat(userEntity.getName()).isEqualTo("NewName");
        assertThat(userEntity.getSurname()).isEqualTo("NewSurname");
    }

    @Test
    void updateEntityEmail() {
        UpdateEmailTo updateEmailTo = new UpdateEmailTo();
        updateEmailTo.setEmail("newEmail@example.com");

        UserEntity userEntity = new UserEntity(100, "Test", "User", "email@example.com", "password", LocalDateTime.of(2019, 12, 27, 13, 0), Collections.singleton(UserRoles.ROLE_USER));
        LocalUserMapper.INSTANCE.updateEntity(updateEmailTo, userEntity);

        assertThat(userEntity.getEmail()).isEqualTo("newemail@example.com");
    }

    @Test
    void updateEntityPassword() {
        UpdatePasswordTo updatePasswordTo = new UpdatePasswordTo();
        updatePasswordTo.setOldPassword("password1");
        updatePasswordTo.setNewPassword("password2");

        UserEntity userEntity = new UserEntity(100, "Test", "User", "email@example.com", "password", LocalDateTime.of(2019, 12, 27, 13, 0), Collections.singleton(UserRoles.ROLE_USER));
        LocalUserMapper.INSTANCE.updateEntity(updatePasswordTo, userEntity, new TestPasswordEncoder());

        assertThat(userEntity.getPassword()).isEqualTo(TestPasswordEncoder.PREFIX + "password2");
    }

    @Test
    void updateEntity() {
        AdminUpdateUserTo adminUpdateUserTo = new AdminUpdateUserTo();
        adminUpdateUserTo.setEmail("Example@example.com");
        adminUpdateUserTo.setName("NewName");
        adminUpdateUserTo.setSurname("NewSurname");
        adminUpdateUserTo.setRoles(Collections.singleton(UserRoles.ROLE_USER));

        Set<UserRoles> roles = new HashSet<>();
        roles.add(UserRoles.ROLE_ADMIN);
        roles.add(UserRoles.ROLE_USER);

        UserEntity userEntity = new UserEntity(100, "Test", "User", "email@email.com", "pass", null, roles);
        LocalUserMapper.INSTANCE.updateEntity(adminUpdateUserTo, userEntity);

        UserEntity expected = new UserEntity(100, "NewName", "NewSurname", "example@example.com", "pass", null, Collections.singleton(UserRoles.ROLE_USER));
        assertThat(userEntity).isEqualToComparingFieldByField(expected);
    }

    @Test
    void toRepresentationModel() {
        UserTo user = new UserTo(100, "Test", "User", "email@example.com", Set.of(UserRoles.ROLE_ADMIN, UserRoles.ROLE_USER));
        UserToRepresentationModel representationModel = LocalUserMapper.INSTANCE.toRepresentationModel(user);

        assertThat(representationModel.getId()).isEqualTo(100);
        assertThat(representationModel.getName()).isEqualTo("Test");
        assertThat(representationModel.getSurname()).isEqualTo("User");
        assertThat(representationModel.getEmail()).isEqualTo("email@example.com");
        assertThat(representationModel.getRoles()).isEqualTo(Set.of(UserRoles.ROLE_ADMIN, UserRoles.ROLE_USER));
    }
}
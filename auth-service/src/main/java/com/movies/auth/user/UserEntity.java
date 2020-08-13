package com.movies.auth.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.movies.common.user.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@ToString(exclude = "password")
public class UserEntity {
    @Id
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private Integer id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private LocalDateTime registered;

    @JsonInclude
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "users_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<UserRoles> roles;
}

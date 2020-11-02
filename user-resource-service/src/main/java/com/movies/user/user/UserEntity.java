package com.movies.user.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.movies.common.user.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotEmpty
    @Size(min = 3, max = 30)
    private String name;

    @NotEmpty
    @Size(min = 3, max = 30)
    private String surname;

    @Email
    private String email;

    @NotEmpty
    @Size(min = 8)
    private String password;

    @NotNull
    private LocalDateTime registered;

    @JsonInclude
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "users_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @Size(min = 1)
    private Set<UserRoles> roles;
}

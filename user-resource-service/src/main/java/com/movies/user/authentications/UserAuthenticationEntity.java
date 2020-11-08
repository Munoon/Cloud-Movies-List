package com.movies.user.authentications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_authentications")
public class UserAuthenticationEntity {
    @Id
    @SequenceGenerator(name = "users_authentications_seq", sequenceName = "users_authentications_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_authentications_seq")
    private Integer id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    public UserAuthenticationEntity(String sessionId) {
        this.sessionId = sessionId;
    }
}

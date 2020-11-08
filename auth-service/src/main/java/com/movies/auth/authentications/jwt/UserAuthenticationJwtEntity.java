package com.movies.auth.authentications.jwt;

import com.movies.auth.authentications.UserAuthenticationEntity;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "token")
@Table(name = "users_authentications_jwt")
public class UserAuthenticationJwtEntity {
    @Id
    @SequenceGenerator(name = "users_authentications_jwt_seq", sequenceName = "users_authentications_jwt_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_authentications_jwt_seq")
    private Integer id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration", nullable = false)
    private Date expiration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authentication_id")
    private UserAuthenticationEntity authentication;

    public UserAuthenticationJwtEntity(String token, Date expiration, UserAuthenticationEntity authentication) {
        this.token = token;
        this.expiration = expiration;
        this.authentication = authentication;
    }
}

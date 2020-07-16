package com.movies.user.user.to;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ToString(exclude = "password")
public class RegisterUserTo {
    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 5)
    private String password;
}

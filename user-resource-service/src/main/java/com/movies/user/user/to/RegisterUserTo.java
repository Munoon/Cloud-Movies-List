package com.movies.user.user.to;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
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

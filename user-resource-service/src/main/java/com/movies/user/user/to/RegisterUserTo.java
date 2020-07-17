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
    @Size(min = 3, max = 30)
    private String name;

    @NotEmpty
    @Size(min = 3, max = 30)
    private String surname;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 8)
    private String password;
}

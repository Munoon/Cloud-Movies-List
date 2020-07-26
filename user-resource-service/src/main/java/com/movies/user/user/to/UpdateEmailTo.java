package com.movies.user.user.to;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UpdateEmailTo {
    @Email
    @NotEmpty
    private String email;
}

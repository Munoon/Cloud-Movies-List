package com.movies.user.user.to;

import com.movies.user.validators.password.ValidatePassword;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UpdatePasswordTo {
    @ValidatePassword
    private String oldPassword;

    @NotEmpty
    @Size(min = 8)
    private String newPassword;
}

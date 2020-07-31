package com.movies.user.user.to;

import com.movies.common.user.UserRoles;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class AdminSaveUserTo {
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
    private Set<UserRoles> roles;
}

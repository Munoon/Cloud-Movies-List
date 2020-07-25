package com.movies.user.user.to;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UpdateProfileTo {
    @NotEmpty
    @Size(min = 3, max = 30)
    private String name;

    @NotEmpty
    @Size(min = 3, max = 30)
    private String surname;
}

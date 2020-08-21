package com.movies.common.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieTo {
    private String id;
    private String name;
    private boolean hasAvatar;
    private LocalDateTime registered;
}

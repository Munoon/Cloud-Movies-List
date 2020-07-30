package com.movies.user.user.to;

import com.movies.common.user.UserRoles;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Data
@Relation(collectionRelation = "users")
public class UserToRepresentationModel extends RepresentationModel<UserToRepresentationModel> {
    private Integer id;

    private String name;

    private String surname;

    private String email;

    private Set<UserRoles> roles;
}

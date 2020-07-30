package com.movies.user.user;

import com.movies.common.user.UserTo;
import com.movies.user.user.to.UserToRepresentationModel;
import com.movies.user.util.mapper.LocalUserMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserToRepresentationModelAssembler implements RepresentationModelAssembler<UserTo, UserToRepresentationModel> {
    @Override
    public UserToRepresentationModel toModel(UserTo entity) {
        return LocalUserMapper.INSTANCE.toRepresentationModel(entity);
    }
}

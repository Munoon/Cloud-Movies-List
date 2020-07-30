package com.movies.user.controller;

import com.movies.common.user.UserMapper;
import com.movies.common.user.UserTo;
import com.movies.user.user.UserService;
import com.movies.user.user.UserToRepresentationModelAssembler;
import com.movies.user.user.to.UserToRepresentationModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AdminUsersController {
    private UserService userService;
    private PagedResourcesAssembler<UserTo> pagedResourcesAssembler;
    private UserToRepresentationModelAssembler representationModelAssembler;

    @GetMapping("/list")
    public PagedModel<UserToRepresentationModel> usersList(
            @SortDefault(sort = "registered", direction = Sort.Direction.DESC) Pageable pageable) {
        if (pageable.getPageSize() > 20) {
            throw new ValidationException("Size can be no more than 20");
        }

        Page<UserTo> userToPage = userService.findAll(pageable)
                .map(UserMapper.INSTANCE::asTo);

        return pagedResourcesAssembler.toModel(userToPage, representationModelAssembler);
    }
}

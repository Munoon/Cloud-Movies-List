package com.movies.user.controller;

import com.movies.common.user.User;
import com.movies.common.user.UserMapper;
import com.movies.common.user.UserTo;
import com.movies.user.user.UserService;
import com.movies.user.user.UserToRepresentationModelAssembler;
import com.movies.user.user.to.AdminCreateUserTo;
import com.movies.user.user.to.AdminUpdateUserTo;
import com.movies.user.user.to.UserToRepresentationModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;

import static com.movies.user.util.SecurityUtils.authUserId;

@Slf4j
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

        log.info("Admin {} get users list: {}", authUserId(), pageable);
        Page<UserTo> userToPage = userService.findAll(pageable)
                .map(UserMapper.INSTANCE::asTo);

        return pagedResourcesAssembler.toModel(userToPage, representationModelAssembler);
    }

    @PostMapping("/create")
    public UserTo createUser(@Valid @RequestBody AdminCreateUserTo adminCreateUserTo) {
        log.info("Admin {} create user {}", authUserId(), adminCreateUserTo);
        User user = userService.createUser(adminCreateUserTo);
        return UserMapper.INSTANCE.asTo(user);
    }

    @GetMapping("/{userId}")
    public UserTo getUserById(@PathVariable int userId) {
        log.info("Admin {} get user {}", authUserId(), userId);
        User user = userService.getById(userId);
        return UserMapper.INSTANCE.asTo(user);
    }

    @PutMapping("/{userId}")
    public UserTo updateUser(@Valid @RequestBody AdminUpdateUserTo userTo, @PathVariable int userId) {
        log.info("Admin {} update user {}: {}", authUserId(), userId, userTo);
        User user = userService.updateUser(userId, userTo);
        return UserMapper.INSTANCE.asTo(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.info("Admin {} delete user {}", authUserId(), userId);
        userService.deleteUserById(userId);
    }
}

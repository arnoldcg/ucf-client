package com.iqsa.ucf.rest.controller;

import com.iqsa.ucf.rest.model.to.UserTO;
import com.iqsa.ucf.rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All the users all returned",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = UserTO.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "The users doesn't has permissions to execute this operation",
                            content = {
                                    @Content()
                            }
                    ),
            }
    )
    @GetMapping(value = "/all")
    public Page<UserTO> getAll(@PageableDefault(value = 20, sort = "id") Pageable pageable) {
        return this.userService.getAll(pageable);
    }

    @Operation(summary = "Return the specific user")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will return a user based on the id.",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = UserTO.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "The users doesn't has permissions to execute this operation",
                            content = {
                                    @Content()
                            }
                    ),
            }
    )
    @GetMapping(value = "/{id}")
    public UserTO getById(@PathVariable("id") Integer id) {
        return this.userService.getById(id);
    }

    @Operation(summary = "Add a user. The company need to be previously created.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will add a user inside the database. " +
                                    "This operation is restricted to specific user with specific role",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = UserTO.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "The users doesn't has permissions to execute this operation",
                            content = {
                                    @Content()
                            }
                    ),
            }
    )
    @PostMapping
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    public UserTO createUser(@RequestBody UserTO userTO) {
        return this.userService.createUser(userTO);
    }

    @Operation(summary = "Update a user")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will update a user inside the database. " +
                                    "This operation is restricted to specific user with specific role",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = UserTO.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "The users doesn't has permissions to execute this operation",
                            content = {
                                    @Content()
                            }
                    ),
            }
    )
    @PutMapping(value = "/{id}")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    public UserTO updateUser(@RequestBody UserTO userTO, @PathVariable("id") Integer id) {
        return this.userService.updateUser(userTO, id);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will delete a user inside the database. " +
                                    "This operation is restricted to specific user with specific role",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = UserTO.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "The users doesn't has permissions to execute this operation",
                            content = {
                                    @Content()
                            }
                    ),
            }
    )
    @DeleteMapping(value = "/{id}")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    public Boolean deleteUser(@PathVariable("id") Integer id) {
        return this.userService.deleteUser(id);
    }
}

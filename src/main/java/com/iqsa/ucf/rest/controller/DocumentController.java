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
@RequestMapping("/document")
public class DocumentController {

    private final UserService userService;

    public DocumentController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all documents")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All the documents are returned",
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

    @Operation(summary = "Return the specific document")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will return a document based on the id.",
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

    @Operation(summary = "Add a document. The user need to be previously created.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will add a document inside the database. " +
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

    @Operation(summary = "Update a document")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will update a document inside the database. " +
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

    @Operation(summary = "Delete a document")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will delete a document inside the database. " +
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

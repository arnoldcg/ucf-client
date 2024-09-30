package com.iqsa.ucf.rest.controller;

import com.iqsa.ucf.rest.model.to.CompanyTO;
import com.iqsa.ucf.rest.service.CompanyService;
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
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Operation(summary = "Get all companies")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All the companies are returned",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = CompanyTO.class)
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
    public Page<CompanyTO> getAll(@PageableDefault(value = 20, sort = "id") Pageable pageable) {
        return this.companyService.getAll(pageable);
    }

    @Operation(summary = "Get an specific company")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will return a company based on the id.",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = CompanyTO.class)
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
    public CompanyTO getById(@PathVariable("id") Integer id) {
        return this.companyService.getById(id);
    }

    @Operation(summary = "Add a company")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will add a company inside the database. " +
                                    "This operation is restricted to specific user with specific role",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = CompanyTO.class)
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
    public CompanyTO createCompany(@RequestBody CompanyTO companyTO) {
        return this.companyService.createCompany(companyTO);
    }

    @Operation(summary = "Add a company with users")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will add a company with defined users. The users will be created as new users. " +
                                    "This operation is restricted to specific user with specific role",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = CompanyTO.class)
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
    @PostMapping("/definition")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    public CompanyTO createCompanyWithUsers(@RequestBody CompanyTO companyTO) {
        return this.companyService.createCompanyWithUsers(companyTO);
    }

    @Operation(summary = "Update a company")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will update a company inside the database. " +
                                    "Only the name and the description of the company can be updated." +
                                    "This operation is restricted to specific user with specific role",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = CompanyTO.class)
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
    public CompanyTO updateCompany(@RequestBody CompanyTO companyTO, @PathVariable("id") Integer id) {
        return this.companyService.updateCompany(companyTO, id);
    }

    @Operation(summary = "Delete a company")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will delete a company inside the database. " +
                                    "This operation is restricted to specific user with specific role",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = CompanyTO.class)
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
    public Boolean deleteCompany(@PathVariable("id") Integer id) {
        return this.companyService.deleteCompany(id);
    }
}

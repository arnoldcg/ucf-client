package com.iqsa.ucf.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqsa.ucf.rest.model.to.DocumentTO;
import com.iqsa.ucf.rest.service.DocumentService;
import com.iqsa.ucf.rest.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/document")
@Log4j2
public class DocumentController {

    private final DocumentService documentService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public DocumentController(DocumentService documentService, FileService fileService, PasswordEncoder passwordEncoder) {
        this.documentService = documentService;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;

        this.objectMapper = new ObjectMapper();
    }

    @Operation(summary = "Get all documents")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All the documents are returned",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = DocumentTO.class)
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
    public Page<DocumentTO> getAll(@PageableDefault(value = 20, sort = "id") Pageable pageable) {
        return this.documentService.getAll(pageable);
    }

    @Operation(summary = "Return the specific document")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will return a document based on the id.",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = DocumentTO.class)
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
    public DocumentTO getById(@PathVariable("id") Integer id) {
        return this.documentService.getById(id);
    }

    @Operation(summary = "Add a document. The user need to be previously created.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will add a document inside the database and upload a file. " +
                                    "This operation is restricted to specific user with specific role",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = DocumentTO.class)
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
    @PostMapping("/upload")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    public Object createDocument(@RequestParam("metadata") String metadata, @RequestParam("file") MultipartFile file) {

        DocumentTO metadataObj;
        try {
            metadataObj = this.objectMapper.readValue(metadata, DocumentTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Metadata parsing error: \n" + e);
        }

        String absolutePath;
        try {
            absolutePath = this.fileService.uploadFile(file, metadataObj.getPassword());
        } catch (IOException e) {
            log.error(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error uploading file to the server. Please check the logs");
        }


        var passwordHash = passwordEncoder.encode(metadataObj.getPassword());
        metadataObj.setAbsolutePath(absolutePath);
        metadataObj.setPassword(passwordHash);
        return this.documentService.createDocument(metadataObj);
    }

    @Operation(summary = "Update a document")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "This endpoint will update a document metadata inside the database. " +
                                    "The password and the owner are not modifiable fields. " +
                                    "This operation is restricted to specific user with specific role",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = DocumentTO.class)
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
    public DocumentTO updateDocument(@RequestBody DocumentTO documentTO, @PathVariable("id") Integer id) {
        return this.documentService.updateDocument(documentTO, id);
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
                                            schema = @Schema(implementation = DocumentTO.class)
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
    public Boolean deleteDocument(@PathVariable("id") Integer id) {
        return this.documentService.deleteDocument(id);
    }
}

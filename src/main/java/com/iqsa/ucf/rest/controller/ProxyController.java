package com.iqsa.ucf.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    private RestTemplate httpRequest;

    @Value("${application.getAllCompanies}")
    private String getAllCompanies;

    @Value("${application.getAllUsers}")
    private String getAllUsers;

    @Value("${application.getAllDocuments}")
    private String getAllDocuments;

    @Value("${application.username}")
    private String username;

    @Value("${application.password}")
    private String password;

    public ProxyController(RestTemplate httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Operation(summary = "Get all companies")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All the companies are returned through a proxy",
                            content = {
                                    @Content(mediaType = "application/json")
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
    @GetMapping(value = "/companies")
    public Object getAllCompanies(@RequestHeader HttpHeaders headers) {
        this.addAuthorizationHeaders(headers);
        HttpEntity template = new HttpEntity(null, headers);
        return this.httpRequest
                .exchange(this.getAllCompanies, HttpMethod.GET, template, Object.class).getBody();
    }

    @Operation(summary = "Get all users")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All the users are returned through a proxy",
                            content = {
                                    @Content(mediaType = "application/json")
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
    @GetMapping(value = "/users")
    public Object getAllUsers(@RequestHeader HttpHeaders headers) {
        this.addAuthorizationHeaders(headers);
        HttpEntity template = new HttpEntity(null, headers);
        return this.httpRequest
                .exchange(this.getAllUsers, HttpMethod.GET, template, Object.class).getBody();
    }


    @Operation(summary = "Get all documents")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All the documents are returned through a proxy",
                            content = {
                                    @Content(mediaType = "application/json")
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
    @GetMapping(value = "/documents")
    public Object getAllDocuments(@RequestHeader HttpHeaders headers) {
        this.addAuthorizationHeaders(headers);
        HttpEntity template = new HttpEntity(null, headers);
        return this.httpRequest
                .exchange(this.getAllDocuments, HttpMethod.GET, template, Object.class).getBody();
    }

    private void addAuthorizationHeaders(HttpHeaders headers) {
        if (!headers.containsKey("Authorization")) {
            var valuePlain = this.username.concat(":").concat(this.password);
            var base64 = Base64.getEncoder().encode(valuePlain.getBytes());
            var strBase64 = new String(base64);
            var headerValue = String.format("Basic %s", strBase64);
            headers.set("Authorization", headerValue);
        }
    }
}

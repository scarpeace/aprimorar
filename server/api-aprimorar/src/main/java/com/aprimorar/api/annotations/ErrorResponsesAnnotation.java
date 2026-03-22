package com.aprimorar.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.ProblemDetail;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProblemDetail.class)
        )
    ),
     @ApiResponse(
        responseCode = "404",
        description = "Recurso não encontrado",
        content = @Content(
            schema = @Schema(implementation = ProblemDetail.class),
            mediaType = "application/json"
            )
    ),
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de negócio",
        content = @Content(
            schema = @Schema(implementation = ProblemDetail.class),
            mediaType = "application/json"
            )
    ),
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProblemDetail.class)
        )
    )
})
public @interface ErrorResponsesAnnotation {}
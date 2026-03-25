package com.aprimorar.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.aprimorar.api.exception.ProblemDetailResponseDTO;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
    @ApiResponse(responseCode = "400", description = "Requisição inválida (erro de validação)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetailResponseDTO.class))),
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetailResponseDTO.class))),
    @ApiResponse(responseCode = "403", description = "Acesso negado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetailResponseDTO.class))),
    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetailResponseDTO.class))),
    @ApiResponse(responseCode = "409", description = "Conflito de regra de negócio",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetailResponseDTO.class))),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
})
public @interface ErrorResponsesAnnotation {
}

package com.aprimorar.api.exception;


import io.swagger.v3.oas.annotations.media.Schema;

public record ProblemDetailResponseDTO(
        @Schema(description = "Tipo do Erro", example = "ERROR_CODE")
        String type,
        @Schema(description = "Título", example = "Error Title")
        String title,
        @Schema(description = "Status HTTP", example = "400")
        Integer status,
        @Schema(description = "Detalhe geral do erro", example = "Error Detail")
        String detail,
        @Schema(description = "Caminho (URI) onde ocorreu", example = "/v1/path")
        String instance
        ) {

}

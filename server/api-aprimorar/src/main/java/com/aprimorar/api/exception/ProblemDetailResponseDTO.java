package com.aprimorar.api.exception;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProblemDetailResponseDTO(
        @Schema(description = "Tipo do Erro", example = "about:blank")
        String type,
        @Schema(description = "Título", example = "Bad Request")
        String title,
        @Schema(description = "Status HTTP", example = "400")
        Integer status,
        @Schema(description = "Detalhe geral do erro", example = "Erro de validação")
        String detail,
        @Schema(description = "Caminho (URI) onde ocorreu", example = "/v1/students")
        String instance,
        @Schema(description = "Dicionário de erros por campo", example = "{\"nome\": \"Nome é obrigatório\", \"cpf\": \"CPF Invalido\"}")
        Map<String, String> errors
        ) {

}

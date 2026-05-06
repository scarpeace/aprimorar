package com.aprimorar.api.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "Um Problem são boas práticas de formato de exceptions em Java")
public record ProblemResponseDTO(
    @Schema(description = "Tipo do Erro", example = "ERROR_CODE") ErrorCode errorCode,
    @Schema(description = "Status", example = "Status") HttpStatus status,
    @Schema(description = "Mensagem do erro") String message,
    @Schema(description = "Caminho (URI) onde ocorreu", example = "/v1/path") String uri
) {

    public ProblemResponseDTO(ErrorCode errorCode, HttpStatus status, String message, String uri) {
        this.errorCode = errorCode;
        this.status = status;
        this.message = message;
        this.uri = uri;
    }
}

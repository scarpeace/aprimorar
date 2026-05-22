package aprimorar.shared.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "Um Problem são boas práticas de formato de exceptions em Java")
public record ProblemResponseDTO(
    @Schema(description = "Status", example = "Status") HttpStatus status,
    @Schema(description = "Mensagem do erro") String message,
    @Schema(description = "Caminho (URI) onde ocorreu", example = "/v1/path") String uri
) {}

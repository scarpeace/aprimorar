package aprimorar.auth.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload de autenticação")
public record AuthRequestDTO(
    @NotBlank(message = "Informe o e-mail")
    @Email(message = "Use um e-mail válido")
    @Schema(description = "E-mail usado no login", example = "admin@aprimorar.local", nullable = false)
    String username,

    @NotBlank(message = "Informe a senha")
    @Schema(description = "Senha do usuário", example = "admin123", nullable = false)
    String password
) {
}

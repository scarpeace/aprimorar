package aprimorar.auth.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload de autenticacao")
public record AuthRequestDTO(
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser um endereço de e-mail válido")
    @Schema(description = "E-mail usado no login", example = "admin@aprimorar.local")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "admin123")
    String password
) {
}

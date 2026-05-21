package aprimorar.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload de autenticacao")
public record AuthRequestDTO(
    @NotBlank(message = "Email e obrigatorio")
    @Email(message = "Email deve ser um endereco de email valido")
    @Schema(description = "Email usado no login", example = "admin@aprimorar.local")
    String email,

    @NotBlank(message = "Senha e obrigatoria")
    @Schema(description = "Senha do usuario", example = "admin123")
    String password
) {
}

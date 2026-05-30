package aprimorar.auth.web.dto;

import aprimorar.shared.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload para criacao de usuario")
public record UserRequestDTO(
        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail deve ser um endereço de e-mail válido")
        @Schema(description = "E-mail usado no login", example = "john@empresa.com")
        String username,
        @NotBlank(message = "Senha é obrigatória")
        @Schema(description = "Senha", example = "password123")
        String password,
        @NotNull(message = "Perfil é obrigatório")
        @Schema(description = "Perfil de acesso", example = "EMPLOYEE")
        Role role) {
}

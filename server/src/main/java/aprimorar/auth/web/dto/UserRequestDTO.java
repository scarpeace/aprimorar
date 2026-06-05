package aprimorar.auth.web.dto;

import aprimorar.shared.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para criação de usuário")
public record UserRequestDTO(
        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail deve ser um endereço de e-mail válido")
        @Schema(description = "E-mail usado no login", example = "usuario@aprimorar.local")
        String username,
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        @Schema(description = "Senha", example = "password123")
        String password,
        @NotNull(message = "Perfil é obrigatório")
        @Schema(description = "Perfil de acesso", example = "EMPLOYEE")
        Role role) {
}

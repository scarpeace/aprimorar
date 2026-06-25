package aprimorar.auth.web.dto;

import aprimorar.shared.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para criação de usuário")
public record UserRequestDTO(
        @NotBlank(message = "Informe o e-mail")
        @Email(message = "Use um e-mail válido")
        @Schema(description = "E-mail usado no login", example = "usuario@aprimorar.local", nullable = false)
        String username,
        @NotBlank(message = "Informe a senha")
        @Size(min = 6, message = "A senha precisa ter ao menos 6 caracteres")
        @Schema(description = "Senha", example = "password123", nullable = false)
        String password,
        @NotNull(message = "Escolha um perfil")
        @Schema(description = "Perfil de acesso", example = "EMPLOYEE", nullable = false)
        Role role) {
}

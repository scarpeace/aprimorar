package aprimorar.auth.api.dto;

import aprimorar.shared.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotBlank(message = "Username e obrigatorio")
        @Email(message = "Username deve ser um endereco de email valido")
        @Schema(description = "Username, obrigatoriamente email", example = "john@empresa.com")
        String username,
        @NotBlank(message = "Senha e obrigatoria")
        @Schema(description = "Senha", example = "password123")
        String password,
        @NotNull(message = "Role e obrigatoria")
        @Schema(description = "Role", example = "EMPLOYEE")
        Role role) {
}

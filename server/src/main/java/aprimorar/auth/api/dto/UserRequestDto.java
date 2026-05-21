package aprimorar.auth.api.dto;

import aprimorar.shared.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserRequestDto(
        @Schema(description = "Username, obrigatóriamente email", example = "john_doe")
        String username,
        @Schema(description = "Senha", example = "password123")
        String password,
        @Schema(description = "Role", example = "EMPLOYEE")
        Role role) {
}

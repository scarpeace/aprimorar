package aprimorar.auth.api.dto;

import aprimorar.shared.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
        @Schema(description = "ID do usuario")
        UUID id,
        @Schema(description = "Username (email)", example = "john@empresa.com")
        String username,
        @Schema(description = "Role do usuario", example = "EMPLOYEE")
        Role role,
        @Schema(description = "Se a conta esta ativa")
        boolean active,
        @Schema(description = "Data de criacao")
        Instant createdAt,
        @Schema(description = "Data de atualizacao")
        Instant updatedAt) {
}

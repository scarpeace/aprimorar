package aprimorar.auth.api.dto;

import aprimorar.shared.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        @Schema(description = "ID do usuário")
        UUID id,
        @Schema(description = "E-mail usado no login", example = "john@empresa.com")
        String username,
        @Schema(description = "Perfil de acesso", example = "EMPLOYEE")
        Role role,
        @Schema(description = "Indica se a conta está ativa")
        boolean active,
        @Schema(description = "Data de criação")
        Instant createdAt,
        @Schema(description = "Data de atualização")
        Instant updatedAt) {
}

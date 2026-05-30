package aprimorar.auth.web.dto;

import aprimorar.shared.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Dados do usuario retornados pela API")
public record UserResponseDTO(
        @Schema(description = "ID do usuario", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,
        @Schema(description = "E-mail usado no login", example = "john@empresa.com")
        String username,
        @Schema(description = "Perfil de acesso", example = "EMPLOYEE")
        Role role,
        @Schema(description = "Indica se a conta esta ativa", example = "true")
        boolean active,
        @Schema(description = "Data de criacao", example = "2024-03-10T15:33:42Z")
        Instant createdAt,
        @Schema(description = "Data de atualizacao", example = "2024-03-11T09:00:00Z")
        Instant updatedAt) {
}

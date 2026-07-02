package aprimorar.auth.web.dto;

import aprimorar.auth.Role;
import aprimorar.auth.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Dados do usuario retornados pela API")
public record UserResponseDTO(
        @NotNull
        @Schema(description = "ID do usuario", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @NotNull
        @Schema(description = "E-mail usado no login", example = "john@empresa.com")
        String username,

        @NotNull
        @Schema(description = "Perfil de acesso", example = "COLABORADOR")
        Role role,

        @NotNull
        @Schema(description = "Indica se a conta está ativa", example = "true")
        boolean active,

        @NotNull
        @Schema(description = "Data de criacao", example = "2024-03-10T15:33:42Z")
        Instant createdAt,

        @NotNull
        @Schema(description = "Data de atualizacao", example = "2024-03-11T09:00:00Z")
        Instant updatedAt) {

    public static UserResponseDTO toDto(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.isActive(),
            user.getCreatedAt(),
            user.getUpdatedAt());
    }
}

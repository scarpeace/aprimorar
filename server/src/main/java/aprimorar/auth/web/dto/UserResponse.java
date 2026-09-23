package aprimorar.auth.web.dto;

import aprimorar.auth.domain.Role;
import aprimorar.auth.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Dados detalhados de um usuário")
public record UserResponse(
    @NotNull
    @Schema(nullable = false, description = "Identificador do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @NotNull
    @Email
    @Schema(nullable = false, description = "E-mail do usuário", example = "secretaria@aprimorar.com")
    String email,

    @NotNull
    @Schema(nullable = false, description = "Perfil de acesso do usuário", example = "SECRETARIA")
    Role role,

    @Schema(nullable = false, description = "Indica se o usuário está habilitado", example = "true")
    boolean enabled
) {

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getRole(),
            user.isEnabled()
        );
    }
}

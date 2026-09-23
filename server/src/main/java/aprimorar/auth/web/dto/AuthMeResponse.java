package aprimorar.auth.web.dto;

import aprimorar.auth.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AuthMeResponse(
    @NotNull
    @Schema(nullable = false, description = "ID do usuário")
    UUID id,

    @NotNull
    @Email
    @Schema(nullable = false, description = "E-mail do usuário")
    String email,

    @NotNull
    @Schema(nullable = false, description = "Perfil do usuário")
    Role role
) {
}

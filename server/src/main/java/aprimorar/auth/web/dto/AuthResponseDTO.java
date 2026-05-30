package aprimorar.auth.web.dto;

import aprimorar.shared.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticacao com access token JWT")
public record AuthResponseDTO(
    @Schema(description = "JWT de acesso", example = "eyJhbGciOiJSUzI1NiJ9...")
    String accessToken,

    @Schema(description = "Tempo de expiração em segundos", example = "28800")
    long expiresIn,

    @Schema(description = "E-mail do usuário autenticado", example = "admin@aprimorar.local")
    String username,

    @Schema(description = "Perfil de acesso do usuário autenticado", example = "ADMIN")
    Role role
) {
}

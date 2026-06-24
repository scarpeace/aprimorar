package aprimorar.auth.web.dto;

import aprimorar.auth.domain.User;
import aprimorar.shared.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Resposta de autenticacao com access token JWT")
public record AuthResponseDTO(
    @NotNull
    @Schema(description = "JWT de acesso", example = "eyJhbGciOiJSUzI1NiJ9...")
    String accessToken,

    @NotNull
    @Schema(description = "Tempo de expiração em segundos", example = "28800")
    long expiresIn,

    @NotNull
    @Schema(description = "Usuário atrelado à autenticação", example = "admin@aprimorar.local")
    UserResponseDTO user
) {

    public static AuthResponseDTO toDto(String accessToken, long expiresIn, User user) {
        return new AuthResponseDTO(accessToken, expiresIn, UserResponseDTO.toDto(user));
    }
}

package aprimorar.auth.web.dto;

import aprimorar.auth.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Resposta de autenticacao com access token JWT")
public record AuthResponseDTO(
    @NotBlank(message = "Token não pode ficar vazio")
    @Schema(description = "JWT de acesso", example = "eyJhbGciOiJSUzI1NiJ9...")
    String accessToken,

    @NotNull
    @PositiveOrZero(message = "O tempo de expiração não pode ser negativo")
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

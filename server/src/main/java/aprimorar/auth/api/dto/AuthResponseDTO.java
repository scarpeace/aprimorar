package aprimorar.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticacao com access token JWT")
public record AuthResponseDTO(
    @Schema(description = "JWT de acesso", example = "eyJhbGciOiJSUzI1NiJ9...")
    String accessToken,

    @Schema(description = "Tempo de expiracao em segundos", example = "28800")
    long expiresIn
) {
}

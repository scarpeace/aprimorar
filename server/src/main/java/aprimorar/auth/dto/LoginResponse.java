package aprimorar.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record LoginResponse(
    @NotNull
    @Schema(nullable = false, description = "Token de acesso JWT")
    String accessToken
) {
}

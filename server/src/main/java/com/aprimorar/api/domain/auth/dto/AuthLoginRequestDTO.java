package com.aprimorar.api.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload para autenticar um usuário interno")
public record AuthLoginRequestDTO(
    @NotBlank(message = "Identificador é obrigatório")
    @Schema(description = "Nome de usuário ou email do colaborador", example = "beatriz.santos")
    String identifier,

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário interno", example = "admin123")
    String password
) {}

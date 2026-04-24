package com.aprimorar.api.domain.auth.dto;

import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Dados do usuário autenticado")
public record AuthCurrentUserResponseDTO(
    @NotNull UUID id,
    @NotNull String username,
    @NotNull String displayName,
    @NotNull String email,
    @NotNull UUID employeeId,
    @NotNull Duty duty,
    @NotNull Role role
) {}

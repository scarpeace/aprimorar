package com.aprimorar.api.domain.auth.dto;

import com.aprimorar.api.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Dados para criação de um novo usuário")
public record UserCreateRequestDTO(
    @NotNull UUID employeeId,
    @NotBlank String username,
    @NotBlank String password,
    @NotNull Role role
) {}

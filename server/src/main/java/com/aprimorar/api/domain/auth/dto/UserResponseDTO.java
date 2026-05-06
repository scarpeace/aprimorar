package com.aprimorar.api.domain.auth.dto;

import com.aprimorar.api.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Resumo dos dados do usuário para listagem")
public record UserResponseDTO(
    UUID id,
    String username,
    String employeeName,
    Role role,
    boolean active
) {}

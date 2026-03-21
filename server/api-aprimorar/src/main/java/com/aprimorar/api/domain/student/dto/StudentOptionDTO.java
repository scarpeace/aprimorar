package com.aprimorar.api.domain.student.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudentOptionDTO(
    UUID id,
    String name
) {
}

package com.aprimorar.api.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDTO(
        Long id,
        String title,
        String description,
        String content,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime startDateTime,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime endDateTime,
        BigDecimal price,
        BigDecimal payment,
        UUID studentId,
        String studentName,
        UUID employeeId,
        String employeeName,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
        Instant createdAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
        Instant updatedAt
) {
}

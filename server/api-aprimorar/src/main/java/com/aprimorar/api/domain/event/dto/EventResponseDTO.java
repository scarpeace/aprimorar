package com.aprimorar.api.domain.event.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDTO(
        UUID id,
        String title,
        String description,
        String content,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        BigDecimal payment,
        UUID studentId,
        String studentName,
        UUID employeeId,
        String employeeName,
        Instant createdAt,
        Instant updatedAt
) {
}

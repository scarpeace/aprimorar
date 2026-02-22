package com.aprimorar.api.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDto(
        Long id,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        BigDecimal price,
        BigDecimal payment,
        UUID studentId,
        String studentName,
        UUID employeeId,
        String employeeName
) {
}

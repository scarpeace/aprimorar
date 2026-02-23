package com.aprimorar.api.dto.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDTO(
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

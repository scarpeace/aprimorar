package com.aprimorar.api.domain.event.command;

import com.aprimorar.api.enums.EventContent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventCommand(
        String title,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        BigDecimal price,
        BigDecimal payment,
        EventContent content
) {
}
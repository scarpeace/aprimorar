package com.aprimorar.api.dto.event;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateEventDTO(
        @NotNull(message = "Event start date/time can't be null")
        @FutureOrPresent(message = "Event start must be in the future")
        LocalDateTime startDateTime,

        @NotNull(message = "Event end date/time can't be null")
        @FutureOrPresent(message = "Event end must be in the future")
        LocalDateTime endDateTime,

        @NotNull(message = "Price can't be null")
        @DecimalMin(value = "0.0", message = "Price must be >= 0")
        BigDecimal price,

        @NotNull(message = "Payment can't be null")
        @DecimalMin(value = "0.0", message = "Payment must be >= 0")
        BigDecimal payment,

        @NotNull(message = "Student ID can't be null")
        UUID studentId,

        @NotNull(message = "Employee ID can't be null")
        UUID employeeId
) {
    @AssertTrue(message = "Payment can't exceed price")
    public boolean isPaymentValid() {
        if (payment == null || price == null) {
            return true;
        }
        return payment.compareTo(price) <= 0;
    }

    @AssertTrue(message = "End date/time must be after start date/time")
    public boolean isEndAfterStart() {
        if (startDateTime == null || endDateTime == null) {
            return true;
        }
        return endDateTime.isAfter(startDateTime);
    }
}

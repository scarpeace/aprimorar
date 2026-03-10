package com.aprimorar.api.domain.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.aprimorar.api.enums.EventContent;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEventDTO(
        @NotBlank(message = "Título do evento é obrigatório")
        @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
        String title,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String description,

        @NotNull(message = "Data/hora de início do evento é obrigatória")
        @FutureOrPresent(message = "Data/hora de início do evento deve ser no presente ou futuro")
        LocalDateTime startDateTime,

        @NotNull(message = "Data/hora de fim do evento é obrigatória")
        @FutureOrPresent(message = "Data/hora de fim do evento deve ser no presente ou futuro")
        LocalDateTime endDateTime,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.0", message = "Preço deve ser maior ou igual a 0")
        BigDecimal price,

        @NotNull(message = "Pagamento é obrigatório")
        @DecimalMin(value = "0.0", message = "Pagamento deve ser maior ou igual a 0")
        BigDecimal payment,

        @NotNull(message = "Conteúdo do evento é obrigatório")
        EventContent content,

        @NotNull(message = "ID do estudante é obrigatório")
        UUID studentId,

        @NotNull(message = "ID do funcionário é obrigatório")
        UUID employeeId
) {
    @AssertTrue(message = "Pagamento não pode ser maior que o preço")
    public boolean isPaymentValid() {
        if (payment == null || price == null) {
            return true;
        }
        return payment.compareTo(price) <= 0;
    }

    @AssertTrue(message = "Data/hora de fim deve ser após a data/hora de início")
    public boolean isEndAfterStart() {
        if (startDateTime == null || endDateTime == null) {
            return true;
        }
        return endDateTime.isAfter(startDateTime);
    }
}

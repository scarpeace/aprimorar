package com.aprimorar.api.domain.event.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.aprimorar.api.enums.EventContent;

import jakarta.validation.constraints.*;

public record EventRequestDTO(
        @NotBlank(message = "Título do evento é obrigatório")
        @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
        String title,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String description,

        @NotNull(message = "Data/hora de início do evento é obrigatória")
        Instant startDate,

        @NotNull(message = "Data/hora de fim do evento é obrigatória")
        Instant endDate,

        @NotNull(message = "Preço é obrigatório")
        @PositiveOrZero(message = "Preço deve ser maior ou igual a 0")
        BigDecimal price,

        @NotNull(message = "Pagamento é obrigatório")
        @PositiveOrZero(message = "Pagamento deve ser maior ou igual a 0")
        BigDecimal payment,

        @NotNull(message = "Conteúdo do evento é obrigatório")
        EventContent content,

        @NotNull(message = "ID do estudante é obrigatório")
        UUID studentId,

        @NotNull(message = "ID do funcionário é obrigatório")
        UUID employeeId) {
}

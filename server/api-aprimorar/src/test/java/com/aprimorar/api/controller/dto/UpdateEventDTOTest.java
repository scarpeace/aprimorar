package com.aprimorar.api.controller.dto;

import com.aprimorar.api.dto.event.UpdateEventDTO;
import com.aprimorar.api.enums.EventContent;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateEventDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("allows partial payload with only description")
    void allowsPartialPayload() {
        UpdateEventDTO dto = new UpdateEventDTO(
                null,
                "Updated description",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Set<ConstraintViolation<UpdateEventDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("rejects payment greater than price when both are provided")
    void rejectsPaymentGreaterThanPrice() {
        UpdateEventDTO dto = new UpdateEventDTO(
                "Physics class",
                null,
                LocalDateTime.of(2027, 6, 1, 10, 0),
                LocalDateTime.of(2027, 6, 1, 11, 0),
                new BigDecimal("50.00"),
                new BigDecimal("100.00"),
                EventContent.ENEM,
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        Set<String> messages = validator.validate(dto).stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertFalse(messages.isEmpty());
        assertTrue(messages.contains("Pagamento não pode ser maior que o preço"));
    }
}

package com.aprimorar.api.controller.dto;

import com.aprimorar.api.dto.event.CreateEventDTO;
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

import static org.junit.jupiter.api.Assertions.*;

class CreateEventDTOTest {

    private static Validator validator;

    private static final LocalDateTime VALID_START = LocalDateTime.of(2027, 6, 1, 10, 0);
    private static final LocalDateTime VALID_END   = LocalDateTime.of(2027, 6, 1, 11, 0);
    private static final LocalDateTime PAST_DT     = LocalDateTime.of(2020, 1, 1, 10, 0);

    private static final BigDecimal VALID_PRICE   = new BigDecimal("100.00");
    private static final BigDecimal VALID_PAYMENT = new BigDecimal("50.00");
    private static final String VALID_TITLE = "This is the first event of the month";
    private static final String VALID_DESCRIPTION = "This is the description of the test event";

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Set<String> messages(Set<ConstraintViolation<CreateEventDTO>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }

    private CreateEventDTO validDto() {
        return eventDto(VALID_START, VALID_END, VALID_PRICE, VALID_PAYMENT, EventContent.ENEM, UUID.randomUUID(), UUID.randomUUID());
    }

    private CreateEventDTO eventDto(
            LocalDateTime start,
            LocalDateTime end,
            BigDecimal price,
            BigDecimal payment,
            EventContent content,
            UUID studentId,
            UUID employeeId
    ) {
        return new CreateEventDTO(VALID_TITLE, VALID_DESCRIPTION, start, end, price, payment, content, studentId, employeeId);
    }

    @Test
    @DisplayName("Should have no violations when all fields are valid")
    void allFieldsValid() {
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(validDto());
        assertTrue(violations.isEmpty());
    }

    // ─── startDateTime ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when startDateTime is null")
    void nullStartDateTime() {
        CreateEventDTO dto = eventDto(null, VALID_END, VALID_PRICE, VALID_PAYMENT, EventContent.ENEM, UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Data/hora de início do evento é obrigatória"));
    }

    @Test
    @DisplayName("Should have 1 violation when startDateTime is in the past")
    void pastStartDateTime() {
        CreateEventDTO dto = eventDto(PAST_DT, VALID_END, VALID_PRICE, VALID_PAYMENT, EventContent.ENEM, UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Data/hora de início do evento deve ser no presente ou futuro"));
    }

    // ─── endDateTime ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when endDateTime is null")
    void nullEndDateTime() {
        CreateEventDTO dto = eventDto(VALID_START, null, VALID_PRICE, VALID_PAYMENT, EventContent.ENEM, UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Data/hora de fim do evento é obrigatória"));
    }

    @Test
    @DisplayName("Should have 1 violation when endDateTime is in the past")
    void pastEndDateTime() {
        CreateEventDTO dto = eventDto(VALID_START, LocalDateTime.of(2020, 1, 1, 11, 0), VALID_PRICE, VALID_PAYMENT, EventContent.ENEM,
                UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Data/hora de fim do evento deve ser no presente ou futuro"));
    }

    // ─── price ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when price is null")
    void nullPrice() {
        CreateEventDTO dto = eventDto(VALID_START, VALID_END, null, VALID_PAYMENT, EventContent.ENEM, UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Preço é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when price is negative")
    void negativePrice() {
        CreateEventDTO dto = eventDto(VALID_START, VALID_END, new BigDecimal("-1.00"), VALID_PAYMENT, EventContent.ENEM,
                UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Preço deve ser maior ou igual a 0"));
    }

    // ─── payment ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when payment is null")
    void nullPayment() {
        CreateEventDTO dto = eventDto(VALID_START, VALID_END, VALID_PRICE, null, EventContent.ENEM, UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Pagamento é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when payment is negative")
    void negativePayment() {
        CreateEventDTO dto = eventDto(VALID_START, VALID_END, VALID_PRICE, new BigDecimal("-1.00"), EventContent.ENEM,
                UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Pagamento deve ser maior ou igual a 0"));
    }

    @Test
    @DisplayName("Should have 1 violation when content is null")
    void nullContent() {
        CreateEventDTO dto = eventDto(VALID_START, VALID_END, VALID_PRICE, VALID_PAYMENT, null, UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Conteúdo do evento é obrigatório"));
    }

    // ─── studentId ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when studentId is null")
    void nullStudentId() {
        CreateEventDTO dto = eventDto(VALID_START, VALID_END, VALID_PRICE, VALID_PAYMENT, EventContent.ENEM, null, UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("ID do estudante é obrigatório"));
    }

    // ─── employeeId ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when employeeId is null")
    void nullEmployeeId() {
        CreateEventDTO dto = eventDto(VALID_START, VALID_END, VALID_PRICE, VALID_PAYMENT, EventContent.ENEM, UUID.randomUUID(), null);
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("ID do funcionário é obrigatório"));
    }

    // ─── cross-field constraints ──────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when payment exceeds price")
    void paymentExceedsPrice() {
        CreateEventDTO dto = eventDto(VALID_START, VALID_END, new BigDecimal("50.00"), new BigDecimal("100.00"), EventContent.ENEM,
                UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Pagamento não pode ser maior que o preço"));
    }

    @Test
    @DisplayName("Should have 1 violation when end date/time is before start date/time")
    void endBeforeStart() {
        CreateEventDTO dto = eventDto(LocalDateTime.of(2027, 6, 1, 12, 0), LocalDateTime.of(2027, 6, 1, 10, 0),
                VALID_PRICE, VALID_PAYMENT, EventContent.ENEM, UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Data/hora de fim deve ser após a data/hora de início"));
    }

    @Test
    @DisplayName("Should have 1 violation when end date/time equals start date/time")
    void endEqualsStart() {
        LocalDateTime sameTime = LocalDateTime.of(2027, 6, 1, 10, 0);
        CreateEventDTO dto = eventDto(sameTime, sameTime, VALID_PRICE, VALID_PAYMENT, EventContent.ENEM, UUID.randomUUID(), UUID.randomUUID());
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Data/hora de fim deve ser após a data/hora de início"));
    }
}

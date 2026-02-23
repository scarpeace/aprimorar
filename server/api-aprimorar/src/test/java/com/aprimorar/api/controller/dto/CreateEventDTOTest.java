package com.aprimorar.api.controller.dto;

import com.aprimorar.api.dto.event.CreateEventDTO;
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
        return new CreateEventDTO(
                VALID_START, VALID_END,
                VALID_PRICE, VALID_PAYMENT,
                UUID.randomUUID(), UUID.randomUUID()
        );
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
        CreateEventDTO dto = new CreateEventDTO(
                null, VALID_END,
                VALID_PRICE, VALID_PAYMENT,
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Event start date/time can't be null"));
    }

    @Test
    @DisplayName("Should have 1 violation when startDateTime is in the past")
    void pastStartDateTime() {
        CreateEventDTO dto = new CreateEventDTO(
                PAST_DT, VALID_END,
                VALID_PRICE, VALID_PAYMENT,
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Event start must be in the future"));
    }

    // ─── endDateTime ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when endDateTime is null")
    void nullEndDateTime() {
        CreateEventDTO dto = new CreateEventDTO(
                VALID_START, null,
                VALID_PRICE, VALID_PAYMENT,
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Event end date/time can't be null"));
    }

    @Test
    @DisplayName("Should have 1 violation when endDateTime is in the past")
    void pastEndDateTime() {
        CreateEventDTO dto = new CreateEventDTO(
                VALID_START, LocalDateTime.of(2020, 1, 1, 11, 0),
                VALID_PRICE, VALID_PAYMENT,
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Event end must be in the future"));
    }

    // ─── price ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when price is null")
    void nullPrice() {
        CreateEventDTO dto = new CreateEventDTO(
                VALID_START, VALID_END,
                null, VALID_PAYMENT,
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Price can't be null"));
    }

    @Test
    @DisplayName("Should have 1 violation when price is negative")
    void negativePrice() {
        CreateEventDTO dto = new CreateEventDTO(
                VALID_START, VALID_END,
                new BigDecimal("-1.00"), VALID_PAYMENT,
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    // ─── payment ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when payment is null")
    void nullPayment() {
        CreateEventDTO dto = new CreateEventDTO(
                VALID_START, VALID_END,
                VALID_PRICE, null,
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Payment can't be null"));
    }

    @Test
    @DisplayName("Should have 1 violation when payment is negative")
    void negativePayment() {
        CreateEventDTO dto = new CreateEventDTO(
                VALID_START, VALID_END,
                VALID_PRICE, new BigDecimal("-1.00"),
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    // ─── studentId ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when studentId is null")
    void nullStudentId() {
        CreateEventDTO dto = new CreateEventDTO(
                VALID_START, VALID_END,
                VALID_PRICE, VALID_PAYMENT,
                null, UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student ID can't be null"));
    }

    // ─── employeeId ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when employeeId is null")
    void nullEmployeeId() {
        CreateEventDTO dto = new CreateEventDTO(
                VALID_START, VALID_END,
                VALID_PRICE, VALID_PAYMENT,
                UUID.randomUUID(), null
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Employee ID can't be null"));
    }

    // ─── cross-field constraints ──────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when payment exceeds price")
    void paymentExceedsPrice() {
        CreateEventDTO dto = new CreateEventDTO(
                VALID_START, VALID_END,
                new BigDecimal("50.00"), new BigDecimal("100.00"),
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Payment can't exceed price"));
    }

    @Test
    @DisplayName("Should have 1 violation when end date/time is before start date/time")
    void endBeforeStart() {
        CreateEventDTO dto = new CreateEventDTO(
                LocalDateTime.of(2027, 6, 1, 12, 0),
                LocalDateTime.of(2027, 6, 1, 10, 0),
                VALID_PRICE, VALID_PAYMENT,
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("End date/time must be after start date/time"));
    }

    @Test
    @DisplayName("Should have 1 violation when end date/time equals start date/time")
    void endEqualsStart() {
        LocalDateTime sameTime = LocalDateTime.of(2027, 6, 1, 10, 0);
        CreateEventDTO dto = new CreateEventDTO(
                sameTime, sameTime,
                VALID_PRICE, VALID_PAYMENT,
                UUID.randomUUID(), UUID.randomUUID()
        );
        Set<ConstraintViolation<CreateEventDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("End date/time must be after start date/time"));
    }
}



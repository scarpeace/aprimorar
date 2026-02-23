package com.aprimorar.api.controller.dto;

import com.aprimorar.api.dto.parent.CreateParentDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CreateParentDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Set<String> messages(Set<ConstraintViolation<CreateParentDTO>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("Should have no violations when all fields are valid")
    void validDto() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name", "parent@email.com", "(11)99999-9999", "123.456.789-01"
        );
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── name ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when name is null")
    void nullName() {
        CreateParentDTO dto = new CreateParentDTO(
                null, "parent@email.com", "(11)99999-9999", "123.456.789-01"
        );
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Parent name can't be null"));
    }

    // ─── email ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when email is null")
    void nullEmail() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name", null, "(11)99999-9999", "123.456.789-01"
        );
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Parent email can't be null"));
    }

    @Test
    @DisplayName("Should have 1 violation when email format is invalid")
    void invalidEmail() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name", "not-an-email", "(11)99999-9999", "123.456.789-01"
        );
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    // ─── contact ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when contact is null")
    void nullContact() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name", "parent@email.com", null, "123.456.789-01"
        );
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Parent contact can't be null"));
    }

    @Test
    @DisplayName("Should have 1 violation when contact format is invalid")
    void invalidContact() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name", "parent@email.com", "123", "123.456.789-01"
        );
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Contact must be in format (XX)XXXXX-XXXX"));
    }

    @Test
    @DisplayName("Should have 0 violations when contact format is valid")
    void validContactFormat() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name", "parent@email.com", "(11)99999-9999", "123.456.789-01"
        );
        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    @DisplayName("Should have 1 violation when contact format has 4-digit middle")
    void invalidContactWithFourDigits() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name", "parent@email.com", "(11)9999-9999", "123.456.789-01"
        );
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Contact must be in format (XX)XXXXX-XXXX"));
    }

    // ─── cpf ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when cpf is null")
    void nullCpf() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name", "parent@email.com", "(11)99999-9999", null
        );
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Parent cpf can't be null"));
    }

    @Test
    @DisplayName("Should have 1 violation when cpf format is invalid")
    void invalidCpf() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name", "parent@email.com", "(11)99999-9999", "12345"
        );
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("CPF must be in format XXX.XXX.XXX-XX"));
    }

    @Test
    @DisplayName("Should have 0 violations when cpf format is valid")
    void validCpfFormat() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name", "parent@email.com", "(11)99999-9999", "123.456.789-01"
        );
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}

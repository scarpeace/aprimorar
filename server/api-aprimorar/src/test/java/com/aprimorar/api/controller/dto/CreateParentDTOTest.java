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

    private CreateParentDTO parentDto(String name, String email, String contact, String cpf) {
        return new CreateParentDTO(name, email, contact, cpf);
    }

    private CreateParentDTO validParentDto() {
        return parentDto("Parent Name", "parent@email.com", "(11)99999-9999", "123.456.789-01");
    }

    @Test
    @DisplayName("Should have no violations when all fields are valid")
    void validDto() {
        CreateParentDTO dto = validParentDto();
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── name ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when name is null")
    void nullName() {
        CreateParentDTO dto = parentDto(null, "parent@email.com", "(11)99999-9999", "123.456.789-01");
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Nome do responsável é obrigatório"));
    }

    // ─── email ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when email is null")
    void nullEmail() {
        CreateParentDTO dto = parentDto("Parent Name", null, "(11)99999-9999", "123.456.789-01");
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Email do responsável é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when email format is invalid")
    void invalidEmail() {
        CreateParentDTO dto = parentDto("Parent Name", "not-an-email", "(11)99999-9999", "123.456.789-01");
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Email deve ser um endereço de email válido"));
    }

    // ─── contact ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when contact is null")
    void nullContact() {
        CreateParentDTO dto = parentDto("Parent Name", "parent@email.com", null, "123.456.789-01");
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Contato do responsável é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when contact format is invalid")
    void invalidContact() {
        CreateParentDTO dto = parentDto("Parent Name", "parent@email.com", "123", "123.456.789-01");
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Contato deve estar no formato (XX)XXXXX-XXXX"));
    }

    @Test
    @DisplayName("Should have 1 violation when contact format has 4-digit middle")
    void invalidContactWithFourDigits() {
        CreateParentDTO dto = parentDto("Parent Name", "parent@email.com", "(11)9999-9999", "123.456.789-01");
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Contato deve estar no formato (XX)XXXXX-XXXX"));
    }

    // ─── cpf ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when cpf is null")
    void nullCpf() {
        CreateParentDTO dto = parentDto("Parent Name", "parent@email.com", "(11)99999-9999", null);
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("CPF do responsável é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when cpf format is invalid")
    void invalidCpf() {
        CreateParentDTO dto = parentDto("Parent Name", "parent@email.com", "(11)99999-9999", "12345");
        Set<ConstraintViolation<CreateParentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("CPF deve estar no formato XXX.XXX.XXX-XX"));
    }

}

package com.aprimorar.api.controller.dto;

import com.aprimorar.api.dto.employee.UpdateEmployeeDTO;
import com.aprimorar.api.enums.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateEmployeeDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("allows partial payload with only role")
    void allowsPartialPayload() {
        UpdateEmployeeDTO dto = new UpdateEmployeeDTO(null, null, null, null, null, null, Role.ADMIN);

        Set<ConstraintViolation<UpdateEmployeeDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("rejects invalid email when provided")
    void rejectsInvalidEmail() {
        UpdateEmployeeDTO dto = new UpdateEmployeeDTO(
                null,
                LocalDate.of(1990, 2, 3),
                null,
                null,
                null,
                "not-an-email",
                null
        );

        Set<String> messages = validator.validate(dto).stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertFalse(messages.isEmpty());
        assertTrue(messages.contains("Email deve ser um endereço de email válido"));
    }
}

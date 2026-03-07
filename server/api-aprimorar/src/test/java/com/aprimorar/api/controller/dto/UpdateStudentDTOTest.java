package com.aprimorar.api.controller.dto;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.student.UpdateStudentDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateStudentDTOTest {

    private static Validator validator;

    private static final CreateAddressDTO VALID_ADDRESS =
            new CreateAddressDTO("Street", "123", null, "District", "City", "ST", "12345-678");

    private static final CreateParentDTO VALID_PARENT =
            new CreateParentDTO("Parent Name", "parent@email.com", "(11)99999-9999", "123.456.789-01");

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Set<String> messages(Set<ConstraintViolation<UpdateStudentDTO>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("allows regular student updates without parent fields")
    void allowsUpdatesWithoutParentFields() {
        UpdateStudentDTO dto = new UpdateStudentDTO(
                "Student Name",
                null,
                null,
                null,
                null,
                null,
                VALID_ADDRESS,
                null,
                null
        );

        Set<ConstraintViolation<UpdateStudentDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("rejects parentId and parent together")
    void rejectsParentIdAndParentTogether() {
        UpdateStudentDTO dto = new UpdateStudentDTO(
                "Student Name",
                null,
                null,
                null,
                null,
                null,
                null,
                UUID.randomUUID(),
                VALID_PARENT
        );

        Set<ConstraintViolation<UpdateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Informe somente um entre parentId e parent"));
    }

    @Test
    @DisplayName("rejects empty update payload")
    void rejectsEmptyPayload() {
        UpdateStudentDTO dto = new UpdateStudentDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Set<ConstraintViolation<UpdateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Pelo menos um campo deve ser informado para atualização"));
    }
}

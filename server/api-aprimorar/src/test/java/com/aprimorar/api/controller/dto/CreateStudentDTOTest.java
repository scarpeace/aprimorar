package com.aprimorar.api.controller.dto;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.enums.Activity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CreateStudentDTOTest {

    private static Validator validator;

    private static final LocalDate VALID_BIRTHDATE  = LocalDate.of(2000, 1, 1);
    private static final LocalDate FUTURE_BIRTHDATE = LocalDate.of(2030, 1, 1);

    private static final CreateAddressDTO VALID_ADDRESS =
            new CreateAddressDTO("Street", "123", null, "District", "City", "ST", "12345-678");

    private static final CreateParentDTO VALID_PARENT =
            new CreateParentDTO("Parent Name", "parent@email.com", "(11)99999-9999", "123.456.789-01");

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Set<String> messages(Set<ConstraintViolation<CreateStudentDTO>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("Should have no violations when all fields are valid")
    void validDto() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── name ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when name is null")
    void nullName() {
        CreateStudentDTO dto = new CreateStudentDTO(
                null, VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student name can't be null"));
    }

    // ─── birthdate ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when birthdate is null")
    void nullBirthdate() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", null, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student birthdate can't be null"));
    }

    @Test
    @DisplayName("Should have 1 violation when birthdate is in the future")
    void futureBirthdate() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", FUTURE_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student birthdate should be in the past"));
    }

    // ─── cpf ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when CPF is null")
    void nullCpf() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, null,
                "Great School", "(11)99999-9999", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student CPF can't be null"));
    }

    @Test
    @DisplayName("Should have 1 violation when CPF format is invalid")
    void invalidCpf() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "12345",
                "Great School", "(11)99999-9999", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("CPF must be in format XXX.XXX.XXX-XX"));
    }

    // ─── school ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when school is blank")
    void blankSchool() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "", "(11)99999-9999", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student school can't be blank"));
    }

    // ─── contact ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when contact is blank")
    void blankContact() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student contact number can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when contact format is invalid")
    void invalidContact() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "123", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Contact must be in format (XX)XXXXX-XXXX"));
    }

    @Test
    @DisplayName("Should have 1 violation when contact format has 4-digit middle")
    void invalidContactWithFourDigits() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)9999-9999", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Contact must be in format (XX)XXXXX-XXXX"));
    }

    // ─── email ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when email is blank")
    void blankEmail() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student email can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when email format is invalid")
    void invalidEmail() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "not-an-email",
                Activity.ENEM, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    // ─── activity ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when activity is null")
    void nullActivity() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                null, VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student activity can't be null"));
    }

    // ─── address ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when address is null")
    void nullAddress() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                Activity.ENEM, null, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student Address can't be null"));
    }

    // ─── parent ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when parent is null")
    void nullParent() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, null
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Student Parent can't be null"));
    }

    // ─── cascaded validations ─────────────────────────────────────────────────

    @Test
    @DisplayName("Should propagate address violations when address has blank street")
    void cascadedAddressViolation() {
        CreateAddressDTO invalidAddress =
                new CreateAddressDTO("", "123", null, "District", "City", "ST", "12345-678");

        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                Activity.ENEM, invalidAddress, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Address street can't be blank"));
    }

    @Test
    @DisplayName("Should propagate parent violations when parent has null name")
    void cascadedParentViolation() {
        CreateParentDTO invalidParent =
                new CreateParentDTO(null, "parent@email.com", "(11)99999-9999", "123.456.789-01");

        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                Activity.ENEM, VALID_ADDRESS, null, invalidParent
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Parent name can't be null"));
    }
}

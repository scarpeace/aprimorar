package com.aprimorar.api.controller.dto;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.student.CreateStudentDTO;
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

    private CreateStudentDTO studentDto(
            String name,
            LocalDate birthdate,
            String cpf,
            String school,
            String contact,
            String email,
            CreateAddressDTO address,
            UUID parentId,
            CreateParentDTO parent
    ) {
        return new CreateStudentDTO(name, birthdate, cpf, school, contact, email, address, parentId, parent);
    }

    private CreateStudentDTO validStudentDto() {
        return studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, null, VALID_PARENT
        );
    }

    @Test
    @DisplayName("Should have no violations when all fields are valid")
    void validDto() {
        CreateStudentDTO dto = validStudentDto();
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── name ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when name is null")
    void nullName() {
        CreateStudentDTO dto = studentDto(
                null, VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Nome do estudante é obrigatório"));
    }

    // ─── birthdate ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when birthdate is null")
    void nullBirthdate() {
        CreateStudentDTO dto = studentDto(
                "Student Name", null, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("A data de nascimento do estudante é obrigatória"));
    }

    @Test
    @DisplayName("Should have 1 violation when birthdate is in the future")
    void futureBirthdate() {
        CreateStudentDTO dto = studentDto(
                "Student Name", FUTURE_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("A data de nascimento do estudante não pode ser no futuro"));
    }

    // ─── cpf ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when CPF is null")
    void nullCpf() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, null,
                "Great School", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("CPF do estudante é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when CPF format is invalid")
    void invalidCpf() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "12345",
                "Great School", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("CPF deve estar no formato XXX.XXX.XXX-XX"));
    }

    // ─── school ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when school is blank")
    void blankSchool() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Escola do estudante é obrigatória"));
    }

    // ─── contact ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when contact is blank")
    void blankContact() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "", "student@email.com",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Contato do estudante é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when contact format is invalid")
    void invalidContact() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "123", "student@email.com",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Contato deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX"));
    }

    @Test
    @DisplayName("Should have 0 violations when contact format has 4-digit middle")
    void validContactWithFourDigits() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)9999-9999", "student@email.com",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── email ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when email is blank")
    void blankEmail() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Email do estudante é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when email format is invalid")
    void invalidEmail() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "not-an-email",
                VALID_ADDRESS, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Email deve ser um endereço de email válido"));
    }

    // ─── address ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when address is null")
    void nullAddress() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                null, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Endereço do estudante é obrigatório"));
    }

    // ─── parent ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when both parentId and parent are null")
    void noParentReference() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, null, null
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Informe parentId ou parent"));
    }

    @Test
    @DisplayName("Should have no violations when parentId is provided and parent is null")
    void parentIdOnly() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, UUID.randomUUID(), null
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should have 1 violation when both parentId and parent are provided")
    void parentIdAndParentTogether() {
        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, UUID.randomUUID(), VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Informe somente um entre parentId e parent"));
    }

    // ─── cascaded validations ─────────────────────────────────────────────────

    @Test
    @DisplayName("Should propagate address violations when address has blank street")
    void cascadedAddressViolation() {
        CreateAddressDTO invalidAddress =
                new CreateAddressDTO("", "123", null, "District", "City", "ST", "12345-678");

        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                invalidAddress, null, VALID_PARENT
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Rua do endereço é obrigatória"));
    }

    @Test
    @DisplayName("Should propagate parent violations when parent has null name")
    void cascadedParentViolation() {
        CreateParentDTO invalidParent =
                new CreateParentDTO(null, "parent@email.com", "(11)99999-9999", "123.456.789-01");

        CreateStudentDTO dto = studentDto(
                "Student Name", VALID_BIRTHDATE, "123.456.789-01",
                "Great School", "(11)99999-9999", "student@email.com",
                VALID_ADDRESS, null, invalidParent
        );
        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("Nome do responsável é obrigatório"));
    }
}

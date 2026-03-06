package com.aprimorar.api.controller.dto;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CreateAddressDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Set<String> messages(Set<ConstraintViolation<CreateAddressDTO>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }

    private CreateAddressDTO addressDto(
            String street,
            String number,
            String complement,
            String district,
            String city,
            String state,
            String zip
    ) {
        return new CreateAddressDTO(street, number, complement, district, city, state, zip);
    }

    private CreateAddressDTO validAddressDto() {
        return addressDto("Main Street", "123", null, "Downtown", "Cityville", "SP", "12345-678");
    }

    @Test
    @DisplayName("Should have no violations when all fields are valid")
    void validDto() {
        CreateAddressDTO dto = validAddressDto();
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── street ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when street is null")
    void nullStreet() {
        CreateAddressDTO dto = addressDto(null, "123", null, "Downtown", "Cityville", "SP", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Rua do endereço é obrigatória"));
    }

    @Test
    @DisplayName("Should have 1 violation when street is blank")
    void blankStreet() {
        CreateAddressDTO dto = addressDto("", "123", null, "Downtown", "Cityville", "SP", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Rua do endereço é obrigatória"));
    }

    // ─── number ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when number is null")
    void nullNumber() {
        CreateAddressDTO dto = addressDto("Main Street", null, null, "Downtown", "Cityville", "SP", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Número do endereço é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when number is blank")
    void blankNumber() {
        CreateAddressDTO dto = addressDto("Main Street", "", null, "Downtown", "Cityville", "SP", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Número do endereço é obrigatório"));
    }

    // ─── district ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when district is null")
    void nullDistrict() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, null, "Cityville", "SP", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Bairro do endereço é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when district is blank")
    void blankDistrict() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, "", "Cityville", "SP", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Bairro do endereço é obrigatório"));
    }

    // ─── city ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when city is null")
    void nullCity() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, "Downtown", null, "SP", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Cidade do endereço é obrigatória"));
    }

    @Test
    @DisplayName("Should have 1 violation when city is blank")
    void blankCity() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, "Downtown", "", "SP", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Cidade do endereço é obrigatória"));
    }

    // ─── state ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when state is null")
    void nullState() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, "Downtown", "Cityville", null, "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Estado do endereço é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when state is blank")
    void blankState() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, "Downtown", "Cityville", "", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Estado do endereço é obrigatório"));
    }

    // ─── zip ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when zip is null")
    void nullZip() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, "Downtown", "Cityville", "SP", null);
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("CEP do endereço é obrigatório"));
    }

    @Test
    @DisplayName("Should have 1 violation when zip is blank")
    void blankZip() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, "Downtown", "Cityville", "SP", "");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(messages(violations).contains("CEP do endereço é obrigatório"));
    }

    @Test
    @DisplayName("Should have 0 violations when zip format is valid with dash")
    void validZipWithDash() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, "Downtown", "Cityville", "SP", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should have 0 violations when zip format is valid with digits only")
    void validZipDigitsOnly() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, "Downtown", "Cityville", "SP", "12345678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should have 1 violation when zip format is invalid")
    void invalidZipFormat() {
        CreateAddressDTO dto = addressDto("Main Street", "123", null, "Downtown", "Cityville", "SP", "1234-567");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("CEP deve estar no formato 00000-000 ou 00000000"));
    }

    // ─── complement (optional) ────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 0 violations when complement is null (complement is optional)")
    void nullComplementAllowed() {
        CreateAddressDTO dto = validAddressDto();
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should have 0 violations when complement is blank (complement is optional)")
    void blankComplementAllowed() {
        CreateAddressDTO dto = addressDto("Main Street", "123", "", "Downtown", "Cityville", "SP", "12345-678");
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}

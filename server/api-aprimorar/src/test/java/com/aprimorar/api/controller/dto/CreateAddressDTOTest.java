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

    @Test
    @DisplayName("Should have no violations when all fields are valid")
    void validDto() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── street ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when street is null")
    void nullStreet() {
        CreateAddressDTO dto = new CreateAddressDTO(
                null, "123", null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address street can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when street is blank")
    void blankStreet() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "", "123", null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address street can't be blank"));
    }

    // ─── number ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when number is null")
    void nullNumber() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", null, null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address number can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when number is blank")
    void blankNumber() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "", null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address number can't be blank"));
    }

    // ─── district ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when district is null")
    void nullDistrict() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", null, null, "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address district can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when district is blank")
    void blankDistrict() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", null, "", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address district can't be blank"));
    }

    // ─── city ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when city is null")
    void nullCity() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", null, "Downtown", null, "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address city can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when city is blank")
    void blankCity() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", null, "Downtown", "", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address city can't be blank"));
    }

    // ─── state ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when state is null")
    void nullState() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", null, "Downtown", "Cityville", null, "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address state can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when state is blank")
    void blankState() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", null, "Downtown", "Cityville", "", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address state can't be blank"));
    }

    // ─── zip ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when zip is null")
    void nullZip() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", null, "Downtown", "Cityville", "SP", null
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address zip code can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when zip is blank")
    void blankZip() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", null, "Downtown", "Cityville", "SP", ""
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address zip code can't be blank"));
    }

    // ─── complement (optional) ────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 0 violations when complement is null (complement is optional)")
    void nullComplementAllowed() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should have 0 violations when complement is blank (complement is optional)")
    void blankComplementAllowed() {
        CreateAddressDTO dto = new CreateAddressDTO(
                "Main Street", "123", "", "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}

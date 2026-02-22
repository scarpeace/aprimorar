package com.aprimorar.api.controller.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CreateAddressDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Set<String> messages(Set<ConstraintViolation<CreateAddressDto>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("Should have no violations when all fields are valid")
    void validDto() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // ─── street ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when street is null")
    void nullStreet() {
        CreateAddressDto dto = new CreateAddressDto(
                null, "123", null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address street can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when street is blank")
    void blankStreet() {
        CreateAddressDto dto = new CreateAddressDto(
                "", "123", null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address street can't be blank"));
    }

    // ─── number ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when number is null")
    void nullNumber() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", null, null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address number can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when number is blank")
    void blankNumber() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "", null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address number can't be blank"));
    }

    // ─── district ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when district is null")
    void nullDistrict() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", null, null, "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address district can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when district is blank")
    void blankDistrict() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", null, "", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address district can't be blank"));
    }

    // ─── city ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when city is null")
    void nullCity() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", null, "Downtown", null, "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address city can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when city is blank")
    void blankCity() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", null, "Downtown", "", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address city can't be blank"));
    }

    // ─── state ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when state is null")
    void nullState() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", null, "Downtown", "Cityville", null, "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address state can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when state is blank")
    void blankState() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", null, "Downtown", "Cityville", "", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address state can't be blank"));
    }

    // ─── zipCode ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 1 violation when zipCode is null")
    void nullZipCode() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", null, "Downtown", "Cityville", "SP", null
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address zip code can't be blank"));
    }

    @Test
    @DisplayName("Should have 1 violation when zipCode is blank")
    void blankZipCode() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", null, "Downtown", "Cityville", "SP", ""
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(messages(violations).contains("Address zip code can't be blank"));
    }

    // ─── complement (optional) ────────────────────────────────────────────────

    @Test
    @DisplayName("Should have 0 violations when complement is null (complement is optional)")
    void nullComplementAllowed() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", null, "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should have 0 violations when complement is blank (complement is optional)")
    void blankComplementAllowed() {
        CreateAddressDto dto = new CreateAddressDto(
                "Main Street", "123", "", "Downtown", "Cityville", "SP", "12345-678"
        );
        Set<ConstraintViolation<CreateAddressDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}


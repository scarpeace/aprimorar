package com.aprimorar.api.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Shared transformation and sanitization methods for all application mappers.
 * Single source of truth — eliminates duplication across StudentMapper, EmployeeMapper, ParentMapper.
 */
@Component
public class MapperUtils {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ========================================
    // SANITIZATION METHODS
    // ========================================

    /** Strips CPF formatting, keeps only digits. Input is guaranteed valid by DTO @Pattern. */
    public String sanitizeCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) return null;
        return cpf.replaceAll("\\D", "");
    }

    /** Trims and lowercases email. */
    public String sanitizeEmail(String email) {
        if (email == null || email.isBlank()) return null;
        return email.trim().toLowerCase();
    }

    /** Strips contact formatting, keeps only digits. Input is guaranteed valid by DTO @Pattern. */
    public String sanitizeContact(String contact) {
        if (contact == null || contact.isBlank()) return null;
        return contact.replaceAll("\\D", "");
    }

    /** Strips CEP formatting, keeps only digits. Input is guaranteed valid by DTO @Pattern. */
    public String sanitizeZip(String zip) {
        if (zip == null || zip.isBlank()) return null;
        return zip.replaceAll("\\D", "");
    }

    // ========================================
    // FORMATTING METHODS
    // ========================================

    /** Formats stored CPF digits to display format: 123.456.789-00 */
    public String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11));
    }

    /** Formats stored contact digits to display format: (61)99923-4523 */
    public String formatContact(String contact) {
        if (contact == null || contact.length() != 11) return contact;
        return String.format("(%s)%s-%s",
                contact.substring(0, 2),
                contact.substring(2, 7),
                contact.substring(7, 11));
    }

    /** Formats stored CEP digits to display format: 12345-678 */
    public String formatZip(String zip) {
        if (zip == null || zip.length() != 8) return zip;
        return String.format("%s-%s", zip.substring(0, 5), zip.substring(5, 8));
    }

    /** Parses stored birthdate string back to LocalDate */
    public LocalDate parseBirthdate(String birthdate) {
        if (birthdate == null || birthdate.isBlank()) return null;
        try {
            return LocalDate.parse(birthdate, DATE_FORMAT);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Birthdate must be in format dd/MM/yyyy", ex);
        }
    }
}

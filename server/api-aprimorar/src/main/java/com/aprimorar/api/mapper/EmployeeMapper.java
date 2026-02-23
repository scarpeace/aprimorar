package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.employee.CreateEmployeeDTO;
import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.entity.Employee;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true)
)
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "birthdate", qualifiedByName = "formatBirthdate")
    @Mapping(target = "cpf", qualifiedByName = "sanitizeCpf")
    @Mapping(target = "contact", qualifiedByName = "sanitizeContact")
    Employee toEntity(CreateEmployeeDTO dto);

    @Mapping(target = "birthdate", qualifiedByName = "parseBirthdate")
    @Mapping(target = "cpf", qualifiedByName = "formatCpf")
    @Mapping(target = "contact", qualifiedByName = "formatContact")
    EmployeeResponseDTO toDto(Employee entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "birthdate", qualifiedByName = "formatBirthdate")
    @Mapping(target = "cpf", qualifiedByName = "sanitizeCpf")
    @Mapping(target = "contact", qualifiedByName = "sanitizeContact")
    void updateFromDto(CreateEmployeeDTO dto, @MappingTarget Employee entity);

    // ========================================
    // SANITIZATION METHODS
    // ========================================

    /**
     * Sanitize CPF: remove formatting, keep only digits
     */
    @Named("sanitizeCpf")
    default String sanitizeCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return null;
        }

        String cleaned = cpf.replaceAll("\\D", "");

        if (cleaned.length() != 11) {
            throw new IllegalArgumentException("CPF must have 11 digits");
        }

        return cleaned;
    }

    /**
     * Sanitize contact: remove formatting, keep only digits
     */
    @Named("sanitizeContact")
    default String sanitizeContact(String contact) {
        if (contact == null || contact.isBlank()) {
            return null;
        }

        String cleaned = contact.replaceAll("\\D", "");

        if (cleaned.length() != 11) {
            throw new IllegalArgumentException("Contact must have 11 digits");
        }

        return cleaned;
    }

    // ========================================
    // TRANSFORMATION METHODS
    // ========================================

    /**
     * Format birthdate for persistence: dd/MM/yyyy
     */
    @Named("formatBirthdate")
    default String formatBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            return null;
        }

        return birthdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Parse birthdate for response: dd/MM/yyyy
     */
    @Named("parseBirthdate")
    default LocalDate parseBirthdate(String birthdate) {
        if (birthdate == null || birthdate.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Birthdate must be in format dd/MM/yyyy", ex);
        }
    }

    /**
     * Format CPF for display: 123.456.789-00
     */
    @Named("formatCpf")
    default String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }

        return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11)
        );
    }

    /**
     * Format contact for display: (61)99923-4523
     */
    @Named("formatContact")
    default String formatContact(String contact) {
        if (contact == null || contact.isBlank()) {
            return null;
        }

        if (contact.length() != 11) {
            throw new IllegalArgumentException("Contact must have 11 digits");
        }

        return String.format("(%s)%s-%s",
                contact.substring(0, 2),
                contact.substring(2, 7),
                contact.substring(7, 11)
        );
    }

}

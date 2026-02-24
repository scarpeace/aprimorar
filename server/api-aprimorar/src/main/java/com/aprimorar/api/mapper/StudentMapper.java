package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.entity.Student;
import org.mapstruct.*;


@Mapper(
        componentModel = "spring",
        uses = {ParentMapper.class, AddressMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true)
)

public interface StudentMapper {

    //Entity -> DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cpf", qualifiedByName = "sanitizeCpf")
    @Mapping(target = "email", qualifiedByName = "sanitizeEmail")
    @Mapping(target = "contact", qualifiedByName = "sanitizeContact")
    Student toEntity(CreateStudentDTO dto);

    //DTO - Entity
    @Mapping(target = "contact", qualifiedByName = "formatContact")
    StudentResponseDTO toDto(Student entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cpf", qualifiedByName = "sanitizeCpf")
    @Mapping(target = "email", qualifiedByName = "sanitizeEmail")
    @Mapping(target = "contact", qualifiedByName = "sanitizeContact")
    void updateFromDto(CreateStudentDTO dto, @MappingTarget Student entity);


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
     * Sanitize email: trim and lowercase
     */
    @Named("sanitizeEmail")
    default String sanitizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        return email.trim().toLowerCase();
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

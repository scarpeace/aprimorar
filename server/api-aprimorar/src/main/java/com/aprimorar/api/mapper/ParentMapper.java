package com.aprimorar.api.mapper;


import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.parent.ParentResponseDTO;
import com.aprimorar.api.entity.Parent;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true)
)
public interface ParentMapper {

    @Mapping(target = "cpf", qualifiedByName = "parentSanitizeCpf")
    @Mapping(target = "email", qualifiedByName = "parentSanitizeEmail")
    @Mapping(target = "contact", qualifiedByName = "parentSanitizeContact")
    Parent toEntity(CreateParentDTO dto);

    @Mapping(target = "cpf", qualifiedByName = "parentFormatCpf")
    @Mapping(target = "contact", qualifiedByName = "parentFormatContact")
    ParentResponseDTO toDto(Parent entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cpf", qualifiedByName = "parentSanitizeCpf")
    @Mapping(target = "email", qualifiedByName = "parentSanitizeEmail")
    @Mapping(target = "contact", qualifiedByName = "parentSanitizeContact")
    void updateFromDto(CreateParentDTO dto, @MappingTarget Parent entity);

    // ========================================
    // SANITIZATION METHODS
    // ========================================

    /**
     * Sanitize CPF: remove formatting, keep only digits
     */
    @Named("parentSanitizeCpf")
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
    @Named("parentSanitizeEmail")
    default String sanitizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        return email.trim().toLowerCase();
    }

    /**
     * Sanitize contact: remove formatting, keep only digits
     */
    @Named("parentSanitizeContact")
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
    @Named("parentFormatCpf")
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
    @Named("parentFormatContact")
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

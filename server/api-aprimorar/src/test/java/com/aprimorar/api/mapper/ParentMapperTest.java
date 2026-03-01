package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.parent.ParentResponseDTO;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.util.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParentMapperTest {

    private ParentMapper mapper;

    private static final String PARENT_NAME = "Parent Name";
    private static final String PARENT_EMAIL = "parent@email.com";
    private static final String PARENT_CONTACT_FORMATTED = "(61)99923-4523";
    private static final String PARENT_CONTACT_RAW = "61999234523";
    private static final String PARENT_CPF_FORMATTED = "123.456.789-01";
    private static final String PARENT_CPF_RAW = "12345678901";

    @BeforeEach
    void setup() {
        mapper = new ParentMapper(new MapperUtils());
    }

    @Test
    @DisplayName("Should sanitize contact, cpf, and email when mapping to entity")
    void toEntity_shouldSanitizeContactCpfAndEmail() {
        CreateParentDTO dto = createParentDto("  Parent@Email.Com ");

        Parent entity = mapper.toEntity(dto);

        assertEquals("parent@email.com", entity.getEmail());
        assertEquals("61999234523", entity.getContact());
        assertEquals("12345678901", entity.getCpf());
    }

    @Test
    @DisplayName("Should format contact and cpf when mapping to DTO")
    void toDto_shouldFormatContactAndCpf() {
        Parent entity = parentEntity();

        ParentResponseDTO dto = mapper.toDto(entity);

        assertEquals("(61)99923-4523", dto.contact());
        assertEquals("123.456.789-01", dto.cpf());
    }

    private CreateParentDTO createParentDto(String email) {
        return new CreateParentDTO(PARENT_NAME, email, PARENT_CONTACT_FORMATTED, PARENT_CPF_FORMATTED);
    }

    private Parent parentEntity() {
        Parent entity = new Parent();
        entity.setName(PARENT_NAME);
        entity.setEmail(PARENT_EMAIL);
        entity.setContact(PARENT_CONTACT_RAW);
        entity.setCpf(PARENT_CPF_RAW);
        return entity;
    }

}

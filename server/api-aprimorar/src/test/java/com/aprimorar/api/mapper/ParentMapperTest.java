package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.parent.ParentResponseDTO;
import com.aprimorar.api.entity.Parent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ParentMapperTest {

    private ParentMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(ParentMapper.class);
    }

    @Test
    @DisplayName("Should sanitize contact, cpf, and email when mapping to entity")
    void toEntity_shouldSanitizeContactCpfAndEmail() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name",
                "  Parent@Email.Com ",
                "(61)99923-4523",
                "123.456.789-01"
        );

        Parent entity = mapper.toEntity(dto);

        assertEquals("parent@email.com", entity.getEmail());
        assertEquals("61999234523", entity.getContact());
        assertEquals("12345678901", entity.getCpf());
    }

    @Test
    @DisplayName("Should format contact and cpf when mapping to DTO")
    void toDto_shouldFormatContactAndCpf() {
        Parent entity = new Parent();
        entity.setName("Parent Name");
        entity.setEmail("parent@email.com");
        entity.setContact("61999234523");
        entity.setCpf("12345678901");

        ParentResponseDTO dto = mapper.toDto(entity);

        assertEquals("(61)99923-4523", dto.contact());
        assertEquals("123.456.789-01", dto.cpf());
    }

    @Test
    @DisplayName("Should throw when contact has invalid length")
    void toEntity_shouldThrowWhenContactHasInvalidLength() {
        CreateParentDTO dto = new CreateParentDTO(
                "Parent Name",
                "parent@email.com",
                "(11)9999-9999",
                "123.456.789-01"
        );

        assertThrows(IllegalArgumentException.class, () -> mapper.toEntity(dto));
    }
}

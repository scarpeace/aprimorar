package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.parent.ParentResponseDTO;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.util.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        @DisplayName("sanitizes contact cpf and email")
        void sanitizesFields() {
            CreateParentDTO dto = createParentDto("  Parent@Email.Com ");

            Parent entity = mapper.toEntity(dto);

            assertEquals("parent@email.com", entity.getEmail());
            assertEquals("61999234523", entity.getContact());
            assertEquals("12345678901", entity.getCpf());
            assertEquals(PARENT_NAME, entity.getName());
        }

        @Test
        @DisplayName("returns null when input is null")
        void returnsNullWhenInputNull() {
            assertNull(mapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("toDto")
    class ToDto {

        @Test
        @DisplayName("formats contact and cpf")
        void formatsContactAndCpf() {
            Parent entity = parentEntity();

            ParentResponseDTO dto = mapper.toDto(entity);

            assertEquals("(61)99923-4523", dto.contact());
            assertEquals("123.456.789-01", dto.cpf());
            assertEquals(PARENT_EMAIL, dto.email());
            assertEquals(PARENT_NAME, dto.name());
        }

        @Test
        @DisplayName("returns null when input is null")
        void returnsNullWhenInputNull() {
            assertNull(mapper.toDto(null));
        }
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

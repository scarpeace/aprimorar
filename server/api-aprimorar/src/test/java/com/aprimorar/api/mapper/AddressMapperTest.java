package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.address.AddressResponseDTO;
import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.util.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AddressMapperTest {

    private AddressMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new AddressMapper(new MapperUtils());
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        @DisplayName("sanitizes zip before persistence")
        void sanitizesZip() {
            CreateAddressDTO dto = new CreateAddressDTO(
                    "Street",
                    "123",
                    null,
                    "District",
                    "City",
                    "SP",
                    "12345-678"
            );

            Address entity = mapper.toEntity(dto);

            assertEquals("12345678", entity.getZip());
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
        @DisplayName("formats zip for response")
        void formatsZip() {
            Address entity = new Address();
            entity.setStreet("Street");
            entity.setNumber("123");
            entity.setDistrict("District");
            entity.setCity("City");
            entity.setState("SP");
            entity.setZip("12345678");

            AddressResponseDTO dto = mapper.toDto(entity);

            assertEquals("12345-678", dto.zip());
        }
    }
}

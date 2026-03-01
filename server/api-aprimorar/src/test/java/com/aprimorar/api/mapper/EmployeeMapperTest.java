package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.employee.CreateEmployeeDTO;
import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.entity.Employee;
import com.aprimorar.api.enums.Role;
import com.aprimorar.api.util.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    private EmployeeMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new EmployeeMapper(new MapperUtils());
    }

    @Test
    @DisplayName("Should format birthdate and sanitize fields when mapping to entity")
    void toEntity_shouldFormatBirthdateAndSanitizeFields() {
        CreateEmployeeDTO dto = new CreateEmployeeDTO(
                "Employee Name",
                LocalDate.of(1990, 2, 3),
                "pix-key",
                "(61)99923-4523",
                "123.456.789-01",
                "email@email.com",
                Role.ADMIN
        );

        Employee entity = mapper.toEntity(dto);

        assertEquals(LocalDate.of(1990,02,03), entity.getBirthdate());
        assertEquals("61999234523", entity.getContact());
        assertEquals("12345678901", entity.getCpf());
    }

    @Test
    @DisplayName("Should parse birthdate and format fields when mapping to DTO")
    void toDto_shouldParseBirthdateAndFormatFields() {
        Employee entity = new Employee();
        entity.setName("Employee Name");
        entity.setBirthdate(LocalDate.of(1990,2, 3));
        entity.setPix("pix-key");
        entity.setContact("61999234523");
        entity.setCpf("12345678901");

        EmployeeResponseDTO dto = mapper.toDto(entity);

        assertEquals(LocalDate.of(1990, 2, 3), dto.birthdate());
        assertEquals("(61)99923-4523", dto.contact());
        assertEquals("123.456.789-01", dto.cpf());
    }

}

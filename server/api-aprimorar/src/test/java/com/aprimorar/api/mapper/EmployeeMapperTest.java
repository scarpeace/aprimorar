package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.employee.CreateEmployeeDTO;
import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.entity.Employee;
import com.aprimorar.api.enums.Role;
import com.aprimorar.api.util.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmployeeMapperTest {

    private EmployeeMapper mapper;

    private static final String EMPLOYEE_NAME = "Employee Name";
    private static final LocalDate EMPLOYEE_BIRTHDATE = LocalDate.of(1990, 2, 3);
    private static final String EMPLOYEE_PIX = "pix-key";
    private static final String EMPLOYEE_CONTACT_FORMATTED = "(61)99923-4523";
    private static final String EMPLOYEE_CONTACT_RAW = "61999234523";
    private static final String EMPLOYEE_CPF_FORMATTED = "123.456.789-01";
    private static final String EMPLOYEE_CPF_RAW = "12345678901";

    @BeforeEach
    void setup() {
        mapper = new EmployeeMapper(new MapperUtils());
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        @DisplayName("sanitizes mapped fields")
        void sanitizesFields() {
            CreateEmployeeDTO dto = validCreateEmployeeDto();

            Employee entity = mapper.toEntity(dto);

            assertEquals(EMPLOYEE_BIRTHDATE, entity.getBirthdate());
            assertEquals("61999234523", entity.getContact());
            assertEquals("12345678901", entity.getCpf());
            assertEquals("email@email.com", entity.getEmail());
            assertEquals(Role.ADMIN, entity.getRole());
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
            Employee entity = validEmployeeEntity();

            EmployeeResponseDTO dto = mapper.toDto(entity);

            assertEquals(EMPLOYEE_BIRTHDATE, dto.birthdate());
            assertEquals("(61)99923-4523", dto.contact());
            assertEquals("123.456.789-01", dto.cpf());
            assertEquals(Role.ADMIN, dto.role());
        }

        @Test
        @DisplayName("returns null when input is null")
        void returnsNullWhenInputNull() {
            assertNull(mapper.toDto(null));
        }
    }

    private CreateEmployeeDTO validCreateEmployeeDto() {
        return new CreateEmployeeDTO(
                EMPLOYEE_NAME,
                EMPLOYEE_BIRTHDATE,
                EMPLOYEE_PIX,
                EMPLOYEE_CONTACT_FORMATTED,
                EMPLOYEE_CPF_FORMATTED,
                "email@email.com",
                Role.ADMIN
        );
    }

    private Employee validEmployeeEntity() {
        Employee entity = new Employee();
        entity.setName(EMPLOYEE_NAME);
        entity.setBirthdate(EMPLOYEE_BIRTHDATE);
        entity.setPix(EMPLOYEE_PIX);
        entity.setContact(EMPLOYEE_CONTACT_RAW);
        entity.setCpf(EMPLOYEE_CPF_RAW);
        entity.setRole(Role.ADMIN);
        return entity;
    }
}

package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.Activity;
import com.aprimorar.api.util.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    private StudentMapper mapper;

    private static final CreateAddressDTO VALID_ADDRESS = new CreateAddressDTO(
            "Street", "123", null, "District", "City", "ST", "12345-678"
    );

    private static final CreateParentDTO VALID_PARENT = new CreateParentDTO(
            "Parent Name", "PARENT@EMAIL.COM", "(61)99923-4523", "123.456.789-01"
    );

    private static final String STUDENT_NAME = "Student Name";
    private static final LocalDate STUDENT_BIRTHDATE = LocalDate.of(2000, 1, 1);
    private static final String STUDENT_CPF_FORMATTED = "123.456.789-01";
    private static final String STUDENT_CONTACT_FORMATTED = "(61)99923-4523";
    private static final String STUDENT_CONTACT_RAW = "61999234523";
    private static final String STUDENT_EMAIL = "student@email.com";

    @BeforeEach
    void setup() {
        MapperUtils mapperUtils = new MapperUtils();
        ParentMapper parentMapper = new ParentMapper(mapperUtils);
        AddressMapper addressMapper = new AddressMapper();

        mapper = new StudentMapper(parentMapper, addressMapper, mapperUtils);
    }

    @Test
    @DisplayName("Should sanitize contact, cpf, and email when mapping to entity")
    void toEntity_shouldSanitizeContactCpfAndEmail() {
        CreateStudentDTO dto = validCreateStudentDto("  STUDENT@EMAIL.COM ");

        Student entity = mapper.toEntity(dto);

        assertEquals("61999234523", entity.getContact());
        assertEquals("12345678901", entity.getCpf());
        assertEquals("student@email.com", entity.getEmail());
    }

    @Test
    @DisplayName("Should format contact when mapping to DTO")
    void toDto_shouldFormatContact() {
        Student entity = validStudentEntity();

        StudentResponseDTO dto = mapper.toDto(entity);

        assertEquals("(61)99923-4523", dto.contact());
        assertEquals("123.456.789-01", dto.cpf());
    }

    private CreateStudentDTO validCreateStudentDto(String email) {
        return new CreateStudentDTO(
                STUDENT_NAME,
                STUDENT_BIRTHDATE,
                STUDENT_CPF_FORMATTED,
                "School",
                STUDENT_CONTACT_FORMATTED,
                email,
                Activity.ENEM,
                VALID_ADDRESS,
                null,
                VALID_PARENT
        );
    }

    private Student validStudentEntity() {
        Student entity = new Student();
        entity.setId(java.util.UUID.randomUUID());
        entity.setName(STUDENT_NAME);
        entity.setContact(STUDENT_CONTACT_RAW);
        entity.setEmail(STUDENT_EMAIL);
        entity.setBirthdate(STUDENT_BIRTHDATE);
        entity.setCpf("12345678901");
        entity.setSchool("School");
        entity.setActivity(Activity.ENEM);
        entity.setParent(new Parent());
        entity.setAddress(new Address());
        entity.setActive(true);
        entity.setCreatedAt(Instant.parse("2025-01-01T00:00:00Z"));
        return entity;
    }

}

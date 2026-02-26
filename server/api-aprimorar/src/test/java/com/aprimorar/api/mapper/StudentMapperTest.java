package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;
import com.aprimorar.api.util.MapperUtils;

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

    @BeforeEach
    void setup() {
        MapperUtils mapperUtils = new MapperUtils();

        ParentMapper parentMapper = Mappers.getMapper(ParentMapper.class);
        ReflectionTestUtils.setField(parentMapper, "mapperUtils", mapperUtils);

        mapper = Mappers.getMapper(StudentMapper.class);
        ReflectionTestUtils.setField(mapper, "parentMapper", parentMapper);
        ReflectionTestUtils.setField(mapper, "addressMapper", Mappers.getMapper(AddressMapper.class));
        ReflectionTestUtils.setField(mapper, "mapperUtils", mapperUtils);
    }

    @Test
    @DisplayName("Should sanitize contact, cpf, and email when mapping to entity")
    void toEntity_shouldSanitizeContactCpfAndEmail() {
        CreateStudentDTO dto = new CreateStudentDTO(
                "Student Name",
                LocalDate.of(2000, 1, 1),
                "123.456.789-01",
                "School",
                "(61)99923-4523",
                "  STUDENT@EMAIL.COM ",
                Activity.ENEM,
                VALID_ADDRESS,
                VALID_PARENT
        );

        Student entity = mapper.toEntity(dto);

        assertEquals("61999234523", entity.getContact());
        assertEquals("12345678901", entity.getCpf());
        assertEquals("student@email.com", entity.getEmail());
    }

    @Test
    @DisplayName("Should format contact when mapping to DTO")
    void toDto_shouldFormatContact() {
        Student entity = new Student();
        entity.setId(java.util.UUID.randomUUID());
        entity.setName("Student Name");
        entity.setContact("61999234523");
        entity.setEmail("student@email.com");
        entity.setBirthdate(LocalDate.of(2000, 1, 1));
        entity.setCpf("12345678901");
        entity.setSchool("School");
        entity.setActivity(Activity.ENEM);
        entity.setParent(new Parent());
        entity.setAddress(new Address());
        entity.setActive(true);
        entity.setCreatedAt(Instant.parse("2025-01-01T00:00:00Z"));

        StudentResponseDTO dto = mapper.toDto(entity);

        assertEquals("(61)99923-4523", dto.contact());
        assertEquals("123.456.789-01", dto.cpf());
    }

}

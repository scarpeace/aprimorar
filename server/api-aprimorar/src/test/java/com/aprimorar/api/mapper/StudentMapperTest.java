package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.util.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentMapperTest {

    private StudentMapper mapper;
    private ParentMapper parentMapper;
    private AddressMapper addressMapper;
    private MapperUtils mapperUtils;

    private static final Instant FIXED_INSTANT = Instant.parse("2025-01-01T02:30:00Z");
    private static final ZoneId SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo");

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
        mapperUtils = new MapperUtils();
        parentMapper = new ParentMapper(mapperUtils);
        addressMapper = new AddressMapper();

        Clock fixedClock = Clock.fixed(FIXED_INSTANT, SAO_PAULO_ZONE);
        mapper = new StudentMapper(parentMapper, addressMapper, mapperUtils, fixedClock);
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        @DisplayName("sanitizes contact cpf and email")
        void sanitizesFields() {
            CreateStudentDTO dto = validCreateStudentDto("  STUDENT@EMAIL.COM ");

            Student entity = mapper.toEntity(dto);

            assertEquals("61999234523", entity.getContact());
            assertEquals("12345678901", entity.getCpf());
            assertEquals("student@email.com", entity.getEmail());
        }
    }

    @Nested
    @DisplayName("toDto")
    class ToDto {

        @Test
        @DisplayName("formats contact cpf and archive fields")
        void formatsAndMapsArchiveFields() {
            Student entity = validStudentEntity();
            Instant archivedAt = Instant.parse("2025-01-02T00:00:00Z");
            Instant lastReactivatedAt = Instant.parse("2025-01-03T00:00:00Z");
            entity.setArchivedAt(archivedAt);
            entity.setLastReactivatedAt(lastReactivatedAt);

            StudentResponseDTO dto = mapper.toDto(entity);

            assertEquals("(61)99923-4523", dto.contact());
            assertEquals("123.456.789-01", dto.cpf());
            assertEquals(archivedAt, dto.archivedAt());
            assertEquals(lastReactivatedAt, dto.lastReactivatedAt());
        }

        @Test
        @DisplayName("computes age using mapper clock")
        void computesAge() {
            Student entity = validStudentEntity();

            StudentResponseDTO dto = mapper.toDto(entity);

            int expectedAge = Period.between(
                    STUDENT_BIRTHDATE,
                    LocalDate.now(Clock.fixed(FIXED_INSTANT, SAO_PAULO_ZONE))
            ).getYears();

            assertEquals(expectedAge, dto.age());
        }

        @Test
        @DisplayName("uses clock zone for age calculation")
        void usesClockZone() {
            Student entity = validStudentEntity();
            StudentMapper utcMapper = new StudentMapper(
                    parentMapper,
                    addressMapper,
                    mapperUtils,
                    Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC)
            );

            StudentResponseDTO saoPauloDto = mapper.toDto(entity);
            StudentResponseDTO utcDto = utcMapper.toDto(entity);

            assertEquals(24, saoPauloDto.age());
            assertEquals(25, utcDto.age());
        }
    }

    @Nested
    @DisplayName("contract")
    class Contract {

        @Test
        @DisplayName("student contract does not expose legacy activity fields")
        void noLegacyActivityFields() {
            assertThrows(NoSuchFieldException.class, () -> Student.class.getDeclaredField("activity"));
            assertThrows(NoSuchFieldException.class, () -> Student.class.getDeclaredField("active"));

            boolean hasActivityInResponse = Arrays.stream(StudentResponseDTO.class.getRecordComponents())
                    .anyMatch(component -> component.getName().equals("activity"));
            boolean hasActiveInResponse = Arrays.stream(StudentResponseDTO.class.getRecordComponents())
                    .anyMatch(component -> component.getName().equals("active"));

            assertFalse(hasActivityInResponse);
            assertFalse(hasActiveInResponse);
        }
    }

    private CreateStudentDTO validCreateStudentDto(String email) {
        return new CreateStudentDTO(
                STUDENT_NAME,
                STUDENT_BIRTHDATE,
                STUDENT_CPF_FORMATTED,
                "School",
                STUDENT_CONTACT_FORMATTED,
                email,
                VALID_ADDRESS,
                null,
                VALID_PARENT
        );
    }

    private Student validStudentEntity() {
        Student entity = new Student();
        entity.setId(UUID.randomUUID());
        entity.setName(STUDENT_NAME);
        entity.setContact(STUDENT_CONTACT_RAW);
        entity.setEmail(STUDENT_EMAIL);
        entity.setBirthdate(STUDENT_BIRTHDATE);
        entity.setCpf("12345678901");
        entity.setSchool("School");
        entity.setParent(new Parent());
        entity.setAddress(new Address());
        entity.setCreatedAt(Instant.parse("2025-01-01T00:00:00Z"));
        return entity;
    }
}

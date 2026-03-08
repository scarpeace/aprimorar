package com.aprimorar.api.service;

import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.dto.student.UpdateStudentDTO;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.exception.domain.ParentNotFoundException;
import com.aprimorar.api.exception.domain.StudentNotFoundException;
import com.aprimorar.api.mapper.ParentMapper;
import com.aprimorar.api.mapper.StudentMapper;
import com.aprimorar.api.repository.ParentRepository;
import com.aprimorar.api.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private static final ZoneId SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo");
    private static final Instant FIXED_NOW = Instant.parse("2026-03-08T12:00:00Z");
    private static final String STUDENT_NAME = "John Doe";
    private static final LocalDate STUDENT_BIRTHDATE = LocalDate.now(SAO_PAULO_ZONE).minusYears(15);
    private static final String STUDENT_CPF_FORMATTED = "123.456.789-01";
    private static final String STUDENT_CPF_RAW = "12345678901";
    private static final String STUDENT_SCHOOL = "School";
    private static final String STUDENT_CONTACT_FORMATTED = "(61)99923-4523";
    private static final String STUDENT_CONTACT_RAW = "61999234523";
    private static final String STUDENT_EMAIL = "john.doe@email.com";
    private static final String PARENT_NAME = "Jane Doe";
    private static final String PARENT_EMAIL = "jane.doe@email.com";
    private static final String PARENT_CONTACT = "(61)99999-9999";
    private static final String PARENT_CPF = "12345678900";

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private ParentRepository parentRepo;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private ParentMapper parentMapper;

    @Mock
    private Clock applicationClock;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private Parent parent;
    private CreateStudentDTO createStudentDtoWithNewParent;
    private CreateStudentDTO createStudentDtoWithParentId;
    private CreateStudentDTO createStudentDtoWithoutParent;
    private UpdateStudentDTO updateStudentDtoWithNewParent;
    private UpdateStudentDTO updateStudentDtoWithParentId;
    private CreateParentDTO createParentDto;
    private StudentResponseDTO studentResponseDto;

    @BeforeEach
    void setUp() {
        lenient().when(applicationClock.instant()).thenReturn(FIXED_NOW);
        lenient().when(applicationClock.getZone()).thenReturn(ZoneOffset.UTC);

        student = validStudentEntity();
        parent = validParentEntity();

        createParentDto = validCreateParentDto();
        createStudentDtoWithNewParent = studentDtoWithParent(createParentDto);
        createStudentDtoWithParentId = studentDtoWithParentId(parent.getId());
        createStudentDtoWithoutParent = studentDtoWithoutParent();
        updateStudentDtoWithNewParent = updateStudentDtoWithParent(createParentDto);
        updateStudentDtoWithParentId = updateStudentDtoWithParentId(parent.getId());

        studentResponseDto = mock(StudentResponseDTO.class);
    }

    @Nested
    @DisplayName("listStudents")
    class ListStudents {

        @Test
        @DisplayName("returns active students page by default")
        void listDefault() {
            Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));
            Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);

            when(studentRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(students);
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            Page<StudentResponseDTO> result = studentService.listStudents(pageable, null, false);

            assertEquals(1, result.getTotalElements());
            assertSame(studentResponseDto, result.getContent().getFirst());
            verify(studentRepo).findAll(any(Specification.class), eq(pageable));
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper);
        }

        @Test
        @DisplayName("returns empty page when no students match")
        void listEmpty() {
            Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));
            Page<Student> students = new PageImpl<>(List.of(), pageable, 0);

            when(studentRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(students);

            Page<StudentResponseDTO> result = studentService.listStudents(pageable, null, false);

            assertEquals(0, result.getTotalElements());
            assertTrue(result.getContent().isEmpty());
            verify(studentRepo).findAll(any(Specification.class), eq(pageable));
            verifyNoMoreInteractions(studentRepo);
            verifyNoInteractions(studentMapper, parentRepo, parentMapper);
        }

        @Test
        @DisplayName("applies trimmed name filter")
        void listWithNameFilter() {
            Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));
            Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);

            when(studentRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(students);
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            Page<StudentResponseDTO> result = studentService.listStudents(pageable, " John ", false);

            assertEquals(1, result.getTotalElements());
            assertSame(studentResponseDto, result.getContent().getFirst());
            verify(studentRepo).findAll(any(Specification.class), eq(pageable));
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper);
            verifyNoInteractions(parentRepo, parentMapper);
        }

        @Test
        @DisplayName("ignores blank name filter")
        void listWithBlankNameFilter() {
            Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));
            Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);

            when(studentRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(students);
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            Page<StudentResponseDTO> result = studentService.listStudents(pageable, "   ", false);

            assertEquals(1, result.getTotalElements());
            assertSame(studentResponseDto, result.getContent().getFirst());
            verify(studentRepo).findAll(any(Specification.class), eq(pageable));
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper);
            verifyNoInteractions(parentRepo, parentMapper);
        }

        @Test
        @DisplayName("includes archived students when includeArchived=true")
        void listIncludingArchived() {
            Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));
            Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);

            when(studentRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(students);
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            Page<StudentResponseDTO> result = studentService.listStudents(pageable, null, true);

            assertEquals(1, result.getTotalElements());
            assertSame(studentResponseDto, result.getContent().getFirst());
            verify(studentRepo).findAll(any(Specification.class), eq(pageable));
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("returns student when found")
        void findByIdFound() {
            when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            StudentResponseDTO result = studentService.findById(student.getId());

            assertSame(studentResponseDto, result);
            verify(studentRepo).findById(student.getId());
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper);
        }

        @Test
        @DisplayName("throws StudentNotFoundException when id does not exist")
        void findByIdNotFound() {
            UUID id = UUID.randomUUID();
            when(studentRepo.findById(id)).thenReturn(Optional.empty());

            assertThrows(StudentNotFoundException.class, () -> studentService.findById(id));

            verify(studentRepo).findById(id);
            verifyNoMoreInteractions(studentRepo);
            verifyNoInteractions(studentMapper);
        }
    }

    @Nested
    @DisplayName("createStudent")
    class CreateStudent {

        @Test
        @DisplayName("creates student with new parent")
        void createWithNewParent() {
            when(studentMapper.toEntity(createStudentDtoWithNewParent)).thenReturn(student);
            when(parentMapper.toEntity(createParentDto)).thenReturn(parent);
            when(parentRepo.save(parent)).thenReturn(parent);
            when(studentRepo.save(student)).thenReturn(student);
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            StudentResponseDTO result = studentService.createStudent(createStudentDtoWithNewParent);

            assertSame(studentResponseDto, result);
            assertNotNull(student.getParent());
            verify(studentMapper).toEntity(createStudentDtoWithNewParent);
            verify(parentMapper).toEntity(createParentDto);
            verify(parentRepo).save(parent);
            verify(studentRepo).save(student);
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);
        }

        @Test
        @DisplayName("creates student with existing parent by id")
        void createWithParentId() {
            when(studentMapper.toEntity(createStudentDtoWithParentId)).thenReturn(student);
            when(parentRepo.findByIdAndActiveTrue(parent.getId())).thenReturn(Optional.of(parent));
            when(studentRepo.save(student)).thenReturn(student);
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            StudentResponseDTO result = studentService.createStudent(createStudentDtoWithParentId);

            assertSame(studentResponseDto, result);
            assertNotNull(student.getParent());
            verify(studentMapper).toEntity(createStudentDtoWithParentId);
            verify(parentRepo).findByIdAndActiveTrue(parent.getId());
            verify(studentRepo).save(student);
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);
        }

        @Test
        @DisplayName("creates student without parent reference")
        void createWithoutParent() {
            when(studentMapper.toEntity(createStudentDtoWithoutParent)).thenReturn(student);
            when(studentRepo.save(student)).thenReturn(student);
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            StudentResponseDTO result = studentService.createStudent(createStudentDtoWithoutParent);

            assertSame(studentResponseDto, result);
            verify(studentMapper).toEntity(createStudentDtoWithoutParent);
            verify(studentRepo).save(student);
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);
        }

        @Test
        @DisplayName("throws ParentNotFoundException when parentId does not exist")
        void createParentNotFound() {
            UUID nonExistentParentId = UUID.randomUUID();
            CreateStudentDTO dtoWithNonExistentParent = studentDtoWithParentId(nonExistentParentId);

            when(studentMapper.toEntity(dtoWithNonExistentParent)).thenReturn(student);
            when(parentRepo.findByIdAndActiveTrue(nonExistentParentId)).thenReturn(Optional.empty());

            assertThrows(ParentNotFoundException.class,
                    () -> studentService.createStudent(dtoWithNonExistentParent));

            verify(studentMapper).toEntity(dtoWithNonExistentParent);
            verify(parentRepo).findByIdAndActiveTrue(nonExistentParentId);
            verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);
        }
    }

    @Nested
    @DisplayName("archive/unarchive")
    class ArchiveAndUnarchive {

        @Test
        @DisplayName("archives active student and updates timestamp")
        void archiveNotArchived() {
            student.setArchivedAt(null);
            Instant before = student.getUpdatedAt();

            when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));

            studentService.archiveStudent(student.getId());

            assertEquals(FIXED_NOW, student.getArchivedAt());
            assertEquals(FIXED_NOW, student.getUpdatedAt());
            verify(studentRepo).findById(student.getId());
            verifyNoMoreInteractions(studentRepo);
            verifyNoInteractions(studentMapper);
        }

        @Test
        @DisplayName("keeps archive idempotent for already archived student")
        void archiveAlreadyArchived() {
            Instant archivedAt = Instant.parse("2025-01-02T00:00:00Z");
            student.setArchivedAt(archivedAt);
            Instant before = student.getUpdatedAt();

            when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));

            studentService.archiveStudent(student.getId());

            assertEquals(archivedAt, student.getArchivedAt());
            assertEquals(before, student.getUpdatedAt());
            verify(studentRepo).findById(student.getId());
            verifyNoMoreInteractions(studentRepo);
            verifyNoInteractions(studentMapper);
        }

        @Test
        @DisplayName("throws StudentNotFoundException when archiving unknown id")
        void archiveNotFound() {
            UUID id = UUID.randomUUID();
            when(studentRepo.findById(id)).thenReturn(Optional.empty());

            assertThrows(StudentNotFoundException.class, () -> studentService.archiveStudent(id));

            verify(studentRepo).findById(id);
            verifyNoMoreInteractions(studentRepo);
            verifyNoInteractions(studentMapper);
        }

        @Test
        @DisplayName("unarchives student and sets lastReactivatedAt")
        void unarchiveStudent() {
            Instant archivedAt = Instant.parse("2025-01-02T00:00:00Z");
            Instant before = student.getUpdatedAt();
            student.setArchivedAt(archivedAt);
            student.setLastReactivatedAt(null);

            when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));

            studentService.unarchiveStudent(student.getId());

            assertNull(student.getArchivedAt());
            assertEquals(FIXED_NOW, student.getLastReactivatedAt());
            assertEquals(FIXED_NOW, student.getUpdatedAt());
            verify(studentRepo).findById(student.getId());
            verifyNoMoreInteractions(studentRepo);
            verifyNoInteractions(studentMapper);
        }

        @Test
        @DisplayName("keeps unarchive idempotent when student already active")
        void unarchiveAlreadyActive() {
            Instant previousReactivation = Instant.parse("2025-01-03T00:00:00Z");
            Instant before = student.getUpdatedAt();

            student.setArchivedAt(null);
            student.setLastReactivatedAt(previousReactivation);

            when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));

            studentService.unarchiveStudent(student.getId());

            assertNull(student.getArchivedAt());
            assertEquals(previousReactivation, student.getLastReactivatedAt());
            assertEquals(before, student.getUpdatedAt());
            verify(studentRepo).findById(student.getId());
            verifyNoMoreInteractions(studentRepo);
            verifyNoInteractions(studentMapper);
        }
    }

    @Nested
    @DisplayName("updateStudent")
    class UpdateStudent {

        @Test
        @DisplayName("updates student with new parent")
        void updateWithNewParent() {
            Instant before = student.getUpdatedAt();

            when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
            when(parentMapper.toEntity(createParentDto)).thenReturn(parent);
            when(parentRepo.save(parent)).thenReturn(parent);
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            StudentResponseDTO result = studentService.updateStudent(student.getId(), updateStudentDtoWithNewParent);

            assertSame(studentResponseDto, result);
            assertNotNull(student.getParent());
            assertEquals(FIXED_NOW, student.getUpdatedAt());

            verify(studentRepo).findById(student.getId());
            verify(studentMapper).updateFromDto(updateStudentDtoWithNewParent, student);
            verify(parentMapper).toEntity(createParentDto);
            verify(parentRepo).save(parent);
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);
        }

        @Test
        @DisplayName("updates student with existing parent by id")
        void updateWithParentId() {
            Instant before = student.getUpdatedAt();

            when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
            when(parentRepo.findByIdAndActiveTrue(parent.getId())).thenReturn(Optional.of(parent));
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            StudentResponseDTO result = studentService.updateStudent(student.getId(), updateStudentDtoWithParentId);

            assertSame(studentResponseDto, result);
            assertNotNull(student.getParent());
            assertEquals(FIXED_NOW, student.getUpdatedAt());

            verify(studentRepo).findById(student.getId());
            verify(studentMapper).updateFromDto(updateStudentDtoWithParentId, student);
            verify(parentRepo).findByIdAndActiveTrue(parent.getId());
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);
        }

        @Test
        @DisplayName("keeps current parent reference when patching nested parent fields")
        void updateExistingNestedParentWithoutCreatingAnotherParent() {
            Instant before = student.getUpdatedAt();
            student.setParent(parent);

            when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
            when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

            StudentResponseDTO result = studentService.updateStudent(student.getId(), updateStudentDtoWithNewParent);

            assertSame(studentResponseDto, result);
            assertSame(parent, student.getParent());
            assertEquals(FIXED_NOW, student.getUpdatedAt());

            verify(studentRepo).findById(student.getId());
            verify(studentMapper).updateFromDto(updateStudentDtoWithNewParent, student);
            verify(studentMapper).toDto(student);
            verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);
        }

        @Test
        @DisplayName("throws StudentNotFoundException when updating unknown id")
        void updateNotFound() {
            UUID id = UUID.randomUUID();
            when(studentRepo.findById(id)).thenReturn(Optional.empty());

            assertThrows(StudentNotFoundException.class,
                    () -> studentService.updateStudent(id, updateStudentDtoWithNewParent));

            verify(studentRepo).findById(id);
            verifyNoMoreInteractions(studentRepo);
            verifyNoInteractions(studentMapper, parentRepo, parentMapper);
        }
    }

    private Student validStudentEntity() {
        Student value = new Student();
        value.setId(UUID.randomUUID());
        value.setName(STUDENT_NAME);
        value.setContact(STUDENT_CONTACT_RAW);
        value.setEmail(STUDENT_EMAIL);
        value.setBirthdate(STUDENT_BIRTHDATE);
        value.setCpf(STUDENT_CPF_RAW);
        value.setSchool(STUDENT_SCHOOL);
        value.setAddress(new Address());
        value.setCreatedAt(Instant.parse("2025-01-01T00:00:00Z"));
        value.setUpdatedAt(Instant.parse("2025-01-01T00:00:00Z"));
        return value;
    }

    private Parent validParentEntity() {
        Parent value = new Parent();
        value.setId(UUID.randomUUID());
        value.setName(PARENT_NAME);
        value.setEmail(PARENT_EMAIL);
        value.setContact(PARENT_CONTACT);
        value.setCpf(PARENT_CPF);
        value.setActive(true);
        return value;
    }

    private CreateParentDTO validCreateParentDto() {
        return new CreateParentDTO(PARENT_NAME, PARENT_EMAIL, PARENT_CONTACT, PARENT_CPF);
    }

    private CreateStudentDTO studentDtoWithParent(CreateParentDTO createParentDTO) {
        return new CreateStudentDTO(
                STUDENT_NAME,
                STUDENT_BIRTHDATE,
                STUDENT_CPF_FORMATTED,
                STUDENT_SCHOOL,
                STUDENT_CONTACT_FORMATTED,
                STUDENT_EMAIL,
                null,
                null,
                createParentDTO
        );
    }

    private CreateStudentDTO studentDtoWithParentId(UUID parentId) {
        return new CreateStudentDTO(
                STUDENT_NAME,
                STUDENT_BIRTHDATE,
                STUDENT_CPF_FORMATTED,
                STUDENT_SCHOOL,
                STUDENT_CONTACT_FORMATTED,
                STUDENT_EMAIL,
                null,
                parentId,
                null
        );
    }

    private CreateStudentDTO studentDtoWithoutParent() {
        return new CreateStudentDTO(
                STUDENT_NAME,
                STUDENT_BIRTHDATE,
                STUDENT_CPF_FORMATTED,
                STUDENT_SCHOOL,
                STUDENT_CONTACT_FORMATTED,
                STUDENT_EMAIL,
                null,
                null,
                null
        );
    }

    private UpdateStudentDTO updateStudentDtoWithParent(CreateParentDTO createParentDTO) {
        return new UpdateStudentDTO(
                STUDENT_NAME,
                STUDENT_BIRTHDATE,
                STUDENT_CPF_FORMATTED,
                STUDENT_SCHOOL,
                STUDENT_CONTACT_FORMATTED,
                STUDENT_EMAIL,
                null,
                null,
                createParentDTO
        );
    }

    private UpdateStudentDTO updateStudentDtoWithParentId(UUID parentId) {
        return new UpdateStudentDTO(
                STUDENT_NAME,
                STUDENT_BIRTHDATE,
                STUDENT_CPF_FORMATTED,
                STUDENT_SCHOOL,
                STUDENT_CONTACT_FORMATTED,
                STUDENT_EMAIL,
                null,
                parentId,
                null
        );
    }
}

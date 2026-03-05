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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private static final ZoneId SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo");
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
        student = validStudentEntity();
        parent = validParentEntity();

        createParentDto = validCreateParentDto();

        createStudentDtoWithNewParent = studentDtoWithParent(createParentDto);

        UUID parentId = parent.getId();
        createStudentDtoWithParentId = studentDtoWithParentId(parentId);

        createStudentDtoWithoutParent = studentDtoWithoutParent();

        updateStudentDtoWithNewParent = updateStudentDtoWithParent(createParentDto);

        updateStudentDtoWithParentId = updateStudentDtoWithParentId(parent.getId());

        studentResponseDto = mock(StudentResponseDTO.class);
    }

    @Test
    @DisplayName("Should list page of students when success")
    void testListStudents() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);
        when(studentRepo.findAll(pageable)).thenReturn(students);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        Page<StudentResponseDTO> result = studentService.listStudents(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(studentResponseDto, result.getContent().getFirst());

        verify(studentRepo).findAll(pageable);
        verify(studentMapper).toDto(student);
        verifyNoMoreInteractions(studentRepo, studentMapper);
    }

    @Test
    @DisplayName("Should return empty page when there are no students in database")
    void testEmptyStudentList() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(), pageable, 0);
        when(studentRepo.findAll(pageable)).thenReturn(students);

        Page<StudentResponseDTO> result = studentService.listStudents(pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(studentRepo).findAll(pageable);
        verifyNoMoreInteractions(studentRepo);
        verifyNoInteractions(studentMapper);
    }

    @Test
    @DisplayName("Should list students when filtering by name")
    void testListStudentsByNameFilter() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);
        when(studentRepo.findByNameContainingIgnoreCase("John", pageable)).thenReturn(students);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        Page<StudentResponseDTO> result = studentService.listStudents(pageable, " John ");

        assertEquals(1, result.getTotalElements());
        assertSame(studentResponseDto, result.getContent().getFirst());

        verify(studentRepo).findByNameContainingIgnoreCase("John", pageable);
        verify(studentMapper).toDto(student);
        verifyNoMoreInteractions(studentRepo, studentMapper);
        verifyNoInteractions(parentRepo, parentMapper);
    }

    @Test
    @DisplayName("Should ignore blank name filter when listing students")
    void testListStudentsWithBlankNameFilter() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);
        when(studentRepo.findAll(pageable)).thenReturn(students);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        Page<StudentResponseDTO> result = studentService.listStudents(pageable, "   ");

        assertEquals(1, result.getTotalElements());
        assertSame(studentResponseDto, result.getContent().getFirst());

        verify(studentRepo).findAll(pageable);
        verify(studentMapper).toDto(student);
        verifyNoMoreInteractions(studentRepo, studentMapper);
        verifyNoInteractions(parentRepo, parentMapper);
    }

    @Test
    @DisplayName("Should list students when filtering by active=true")
    void testListActiveStudents() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);
        when(studentRepo.findByActive(true, pageable)).thenReturn(students);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        Page<StudentResponseDTO> result = studentService.listStudents(pageable, null, true);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(studentResponseDto, result.getContent().getFirst());

        verify(studentRepo).findByActive(true, pageable);
        verify(studentMapper).toDto(student);
        verifyNoMoreInteractions(studentRepo, studentMapper);
    }

    @Test
    @DisplayName("Should list active students when filtering by name")
    void testListActiveStudentsByNameFilter() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);
        when(studentRepo.findByActiveAndNameContainingIgnoreCase(true, "John", pageable)).thenReturn(students);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        Page<StudentResponseDTO> result = studentService.listStudents(pageable, "John", true);

        assertEquals(1, result.getTotalElements());
        assertSame(studentResponseDto, result.getContent().getFirst());

        verify(studentRepo).findByActiveAndNameContainingIgnoreCase(true, "John", pageable);
        verify(studentMapper).toDto(student);
        verifyNoMoreInteractions(studentRepo, studentMapper);
        verifyNoInteractions(parentRepo, parentMapper);
    }

    @Test
    @DisplayName("Should return empty page when filtering by active=true and there are no matches")
    void testEmptyActiveStudentList() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(), pageable, 0);
        when(studentRepo.findByActive(true, pageable)).thenReturn(students);

        Page<StudentResponseDTO> result = studentService.listStudents(pageable, null, true);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(studentRepo).findByActive(true, pageable);
        verifyNoMoreInteractions(studentRepo);
        verifyNoInteractions(studentMapper);
    }

    @Test
    @DisplayName("Should get student when success")
    void testFindByIdFound() {
        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        StudentResponseDTO result = studentService.findById(student.getId());

        assertSame(studentResponseDto, result);
        verify(studentRepo).findById(student.getId());
        verify(studentMapper).toDto(student);
        verifyNoMoreInteractions(studentRepo, studentMapper);
    }

    @Test
    @DisplayName("Should throw exception when ID not found")
    void testFindByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(studentRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.findById(id));

        verify(studentRepo).findById(id);
        verifyNoMoreInteractions(studentRepo);
        verifyNoInteractions(studentMapper);
    }

    @Test
    @DisplayName("Should create student with new parent when success")
    void testCreateStudentWithNewParent() {
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
    @DisplayName("Should create student with existing parent when parentId is provided")
    void testCreateStudentWithExistingParent() {
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
    @DisplayName("Should create student when parent data is not validated at service boundary")
    void testCreateStudentWithoutParent() {
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
    @DisplayName("Should throw ParentNotFoundException when parentId does not exist")
    void testCreateStudentParentNotFound() {
        UUID nonExistentParentId = UUID.randomUUID();
        CreateStudentDTO dtoWithNonExistentParent = studentDtoWithParentId(nonExistentParentId);

        when(studentMapper.toEntity(dtoWithNonExistentParent)).thenReturn(student);
        when(parentRepo.findByIdAndActiveTrue(nonExistentParentId)).thenReturn(Optional.empty());

        assertThrows(ParentNotFoundException.class, () -> 
                studentService.createStudent(dtoWithNonExistentParent));

        verify(studentMapper).toEntity(dtoWithNonExistentParent);
        verify(parentRepo).findByIdAndActiveTrue(nonExistentParentId);
        verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);
    }

    @Test
    @DisplayName("Should deactivate student when student is active (Soft Delete)")
    void testDeleteStudentActive() {
        student.setActive(true);
        Instant before = student.getUpdatedAt();

        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));

        studentService.softDeleteStudent(student.getId());

        assertFalse(student.getActive());
        assertNotNull(student.getUpdatedAt());
        assertTrue(student.getUpdatedAt().isAfter(before) || !student.getUpdatedAt().equals(before));

        verify(studentRepo).findById(student.getId());
        verifyNoMoreInteractions(studentRepo);
        verifyNoInteractions(studentMapper);
    }

    @Test
    @DisplayName("Should not change updatedAt when student is already inactive")
    void testDeleteStudentInactive() {
        student.setActive(false);
        Instant before = student.getUpdatedAt();

        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));

        studentService.softDeleteStudent(student.getId());

        assertFalse(student.getActive());
        assertEquals(before, student.getUpdatedAt());

        verify(studentRepo).findById(student.getId());
        verifyNoMoreInteractions(studentRepo);
        verifyNoInteractions(studentMapper);
    }

    @Test
    @DisplayName("Should throw exception when ID not found for deletion")
    void testDeleteStudentNotFound() {
        UUID id = UUID.randomUUID();
        when(studentRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.softDeleteStudent(id));

        verify(studentRepo).findById(id);
        verifyNoMoreInteractions(studentRepo);
        verifyNoInteractions(studentMapper);
    }

    @Test
    @DisplayName("Should update student with new parent when success")
    void testUpdateStudentWithNewParent() {
        Instant before = student.getUpdatedAt();

        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        when(parentMapper.toEntity(createParentDto)).thenReturn(parent);
        when(parentRepo.save(parent)).thenReturn(parent);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        StudentResponseDTO result = studentService.updateStudent(student.getId(), updateStudentDtoWithNewParent);

        assertSame(studentResponseDto, result);
        assertNotNull(student.getParent());
        verify(studentRepo).findById(student.getId());
        verify(studentMapper).updateFromDto(updateStudentDtoWithNewParent, student);
        verify(parentMapper).toEntity(createParentDto);
        verify(parentRepo).save(parent);
        verify(studentMapper).toDto(student);
        verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);

        assertNotNull(student.getUpdatedAt());
        assertNotEquals(before, student.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update student with existing parent when parentId is provided")
    void testUpdateStudentWithExistingParent() {
        Instant before = student.getUpdatedAt();

        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        when(parentRepo.findByIdAndActiveTrue(parent.getId())).thenReturn(Optional.of(parent));
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        StudentResponseDTO result = studentService.updateStudent(student.getId(), updateStudentDtoWithParentId);

        assertSame(studentResponseDto, result);
        assertNotNull(student.getParent());
        verify(studentRepo).findById(student.getId());
        verify(studentMapper).updateFromDto(updateStudentDtoWithParentId, student);
        verify(parentRepo).findByIdAndActiveTrue(parent.getId());
        verify(studentMapper).toDto(student);
        verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);

        assertNotNull(student.getUpdatedAt());
        assertNotEquals(before, student.getUpdatedAt());
    }

    @Test
    @DisplayName("Should not update student when student not found")
    void testUpdateStudentNotFound() {
        UUID id = UUID.randomUUID();
        when(studentRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(id, updateStudentDtoWithNewParent));

        verify(studentRepo).findById(id);
        verifyNoMoreInteractions(studentRepo);
        verifyNoInteractions(studentMapper);
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
        value.setActive(true);
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

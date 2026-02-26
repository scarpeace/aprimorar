package com.aprimorar.api.service;

import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.Activity;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

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
    private CreateParentDTO createParentDto;
    private StudentResponseDTO studentResponseDto;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(UUID.randomUUID());
        student.setName("John Doe");
        student.setContact("61999234523");
        student.setEmail("john.doe@email.com");
        student.setBirthdate(LocalDate.of(2000, 1, 1));
        student.setCpf("12345678901");
        student.setSchool("School");
        student.setActivity(Activity.ENEM);
        student.setAddress(new Address());
        student.setActive(true);
        student.setCreatedAt(Instant.parse("2025-01-01T00:00:00Z"));
        student.setUpdatedAt(Instant.parse("2025-01-01T00:00:00Z"));

        parent = new Parent();
        parent.setId(UUID.randomUUID());
        parent.setName("Jane Doe");
        parent.setEmail("jane.doe@email.com");
        parent.setContact("(61)99999-9999");
        parent.setCpf("12345678900");
        parent.setActive(true);

        createParentDto = new CreateParentDTO(
                "Jane Doe",
                "jane.doe@email.com",
                "(61)99999-9999",
                "12345678900"
        );

        createStudentDtoWithNewParent = new CreateStudentDTO(
                "John Doe",
                LocalDate.of(2000, 1, 1),
                "123.456.789-01",
                "School",
                "(61)99923-4523",
                "john.doe@email.com",
                Activity.ENEM,
                null,
                null,
                createParentDto
        );

        UUID parentId = parent.getId();
        createStudentDtoWithParentId = new CreateStudentDTO(
                "John Doe",
                LocalDate.of(2000, 1, 1),
                "123.456.789-01",
                "School",
                "(61)99923-4523",
                "john.doe@email.com",
                Activity.ENEM,
                null,
                parentId,
                null
        );

        createStudentDtoWithoutParent = new CreateStudentDTO(
                "John Doe",
                LocalDate.of(2000, 1, 1),
                "123.456.789-01",
                "School",
                "(61)99923-4523",
                "john.doe@email.com",
                Activity.ENEM,
                null,
                null,
                null
        );

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
    @DisplayName("Should list active students page when success")
    void testListActiveStudents() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);
        when(studentRepo.findAllByActiveTrue(pageable)).thenReturn(students);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        Page<StudentResponseDTO> result = studentService.listActiveStudents(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(studentResponseDto, result.getContent().getFirst());

        verify(studentRepo).findAllByActiveTrue(pageable);
        verify(studentMapper).toDto(student);
        verifyNoMoreInteractions(studentRepo, studentMapper);
    }

    @Test
    @DisplayName("Should return empty page when there are no active students in database")
    void testEmptyActiveStudentList() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(), pageable, 0);
        when(studentRepo.findAllByActiveTrue(pageable)).thenReturn(students);

        Page<StudentResponseDTO> result = studentService.listActiveStudents(pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(studentRepo).findAllByActiveTrue(pageable);
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
    @DisplayName("Should throw exception when creating student without parent")
    void testCreateStudentWithoutParent() {
        when(studentMapper.toEntity(createStudentDtoWithoutParent)).thenReturn(student);

        assertThrows(IllegalArgumentException.class, () -> 
                studentService.createStudent(createStudentDtoWithoutParent));

        verify(studentMapper).toEntity(createStudentDtoWithoutParent);
        verifyNoMoreInteractions(studentRepo, studentMapper, parentRepo, parentMapper);
    }

    @Test
    @DisplayName("Should throw ParentNotFoundException when parentId does not exist")
    void testCreateStudentParentNotFound() {
        UUID nonExistentParentId = UUID.randomUUID();
        CreateStudentDTO dtoWithNonExistentParent = new CreateStudentDTO(
                "John Doe",
                LocalDate.of(2000, 1, 1),
                "123.456.789-01",
                "School",
                "(61)99923-4523",
                "john.doe@email.com",
                Activity.ENEM,
                null,
                nonExistentParentId,
                null
        );

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

        StudentResponseDTO result = studentService.updateStudent(student.getId(), createStudentDtoWithNewParent);

        assertSame(studentResponseDto, result);
        assertNotNull(student.getParent());
        verify(studentRepo).findById(student.getId());
        verify(studentMapper).updateFromDto(createStudentDtoWithNewParent, student);
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

        StudentResponseDTO result = studentService.updateStudent(student.getId(), createStudentDtoWithParentId);

        assertSame(studentResponseDto, result);
        assertNotNull(student.getParent());
        verify(studentRepo).findById(student.getId());
        verify(studentMapper).updateFromDto(createStudentDtoWithParentId, student);
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

        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(id, createStudentDtoWithNewParent));

        verify(studentRepo).findById(id);
        verifyNoMoreInteractions(studentRepo);
        verifyNoInteractions(studentMapper);
    }
}

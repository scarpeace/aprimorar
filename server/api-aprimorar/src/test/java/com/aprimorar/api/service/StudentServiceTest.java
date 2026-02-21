package com.aprimorar.api.service;

import com.aprimorar.api.controller.dto.CreateStudentDto;
import com.aprimorar.api.controller.dto.StudentResponseDto;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.Activity;
import com.aprimorar.api.exception.domain.StudentNotFoundException;
import com.aprimorar.api.mapper.StudentMapper;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepo;
    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private CreateStudentDto createStudentDto;
    private StudentResponseDto studentResponseDto;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(UUID.randomUUID());
        student.setName("John Doe");
        student.setContact("123456789");
        student.setEmail("john.doe@email.com");
        student.setBirthdate(LocalDate.of(2000, 1, 1));
        student.setCpf("12345678901");
        student.setSchool("School");
        student.setActivity(Activity.ENEM);
        student.setParent(new Parent());
        student.setAddress(new Address());
        student.setActive(true);
        student.setCreatedAt(Instant.now());
        student.setUpdatedAt(Instant.now());

        createStudentDto = mock(CreateStudentDto.class);
        studentResponseDto = mock(StudentResponseDto.class);
    }

    @Test
    @DisplayName("Should list page of students when success")
    void testListStudents() {
        Pageable pageable = PageRequest.of(0,20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(student));
        when(studentRepo.findAll(pageable)).thenReturn(students);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        Page<StudentResponseDto> result = studentService.listStudents(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(studentResponseDto, result.getContent().get(0));
    }

    @Test
    @DisplayName("Should return empty page when there are no students in database")
    void testEmptyStudentList(){
        Pageable pageable = PageRequest.of(0,20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of());
        when(studentRepo.findAll(pageable)).thenReturn(students);

        Page<StudentResponseDto> result = studentService.listStudents(pageable);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Should list active students page when success")
    void testListActiveStudents(){
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Student> students = new PageImpl<>(List.of(student));
        when(studentRepo.findAllByActiveTrue(pageable)).thenReturn(students);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        Page<StudentResponseDto> result = studentService.listActiveStudents(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(studentResponseDto, result.getContent().get(0));

    }


    @Test
    @DisplayName("Should get student when success")
    void testFindByIdFound() {
        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        StudentResponseDto result = studentService.findById(student.getId());
        assertEquals(studentResponseDto, result);
    }

    @Test
    @DisplayName("Should throw exception when ID not found")
    void testFindByIdNotFound() {
        when(studentRepo.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should create student when success")
    void testCreateStudent() {
        when(studentMapper.toEntity(createStudentDto)).thenReturn(student);
        when(studentRepo.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        StudentResponseDto result = studentService.createStudent(createStudentDto);
        assertEquals(studentResponseDto, result);
    }

    @Test
    @DisplayName("Should deactivate student when student is active (Soft Delete)")
    void testDeleteStudentActive() {
        student.setActive(true);
        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));

        studentService.softDeleteStudent(student.getId());

        assertFalse(student.getActive());
        verify(studentRepo).findById(student.getId());
    }

    @Test
    @DisplayName("Should not deactivate when student is already inactive")
    void testDeleteStudentInactive() {
        student.setActive(false);
        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));

        studentService.softDeleteStudent(student.getId());

        assertFalse(student.getActive());
        verify(studentRepo).findById(student.getId());
    }

    @Test
    @DisplayName("Should throw exception when ID not found for deletion")
    void testDeleteStudentNotFound() {
        when(studentRepo.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.softDeleteStudent(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should update student when success")
    void testUpdateStudent() {
        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        doAnswer(invocation -> null).when(studentMapper).updateFromDto(createStudentDto, student);
        when(studentMapper.toDto(student)).thenReturn(studentResponseDto);

        StudentResponseDto result = studentService.updateStudent(student.getId(), createStudentDto);
        assertEquals(studentResponseDto, result);
        verify(studentMapper).updateFromDto(createStudentDto, student);
    }

    @Test
    @DisplayName("Should not update student when student not found")
    void testUpdateStudentNotFound() {
        when(studentRepo.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(UUID.randomUUID(), createStudentDto));
    }
}

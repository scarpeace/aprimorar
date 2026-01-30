package com.aprimorar.api.service;

import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.Activity;
import com.aprimorar.api.mapper.AddressMapper;
import com.aprimorar.api.mapper.ParentMapper;
import com.aprimorar.api.mapper.StudentMapper;
import com.aprimorar.api.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepo;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentRequestDto studentRequestDto;
    private StudentReponseDto studentReponseDto;

    @BeforeEach
    void setUp() {
        student = new Student(UUID.randomUUID(), "John Doe", "123456789", new Date(), "12345678901", "School", Activity.ENEM, new Parent(), new Address(), Instant.now(), Instant.now(), true);
        studentRequestDto = mock(StudentRequestDto.class);
        studentReponseDto = mock(StudentReponseDto.class);
    }

    @Test
    @DisplayName("Should list students when success")
    void testListStudents() {
        List<Student> students = List.of(student);
        when(studentRepo.findAll()).thenReturn(students);
        try (var mocked = mockStatic(StudentMapper.class)) {
            mocked.when(() -> StudentMapper.toDto(student)).thenReturn(studentReponseDto);
            List<StudentReponseDto> result = studentService.listStudents();
            assertEquals(1, result.size());
            assertEquals(studentReponseDto, result.get(0));
        }
    }


    @Test
    @DisplayName("Should get student when success")
    void testFindByIdFound() {
        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        try (var mocked = mockStatic(StudentMapper.class)) {
            mocked.when(() -> StudentMapper.toDto(student)).thenReturn(studentReponseDto);
            StudentReponseDto result = studentService.findById(student.getId().toString());
            assertEquals(studentReponseDto, result);
        }
    }

    @Test
    @DisplayName("Should throw exception when ID not found")
    void testFindByIdNotFound() {
        when(studentRepo.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> studentService.findById(UUID.randomUUID().toString()));
    }

    @Test
    @DisplayName("Should create student when success")
    void testCreateStudent() {
        try (var mocked = mockStatic(StudentMapper.class)) {
            mocked.when(() -> StudentMapper.toEntity(studentRequestDto)).thenReturn(student);
            when(studentRepo.save(student)).thenReturn(student);
            mocked.when(() -> StudentMapper.toDto(student)).thenReturn(studentReponseDto);
            StudentReponseDto result = studentService.createStudent(studentRequestDto);
            assertEquals(studentReponseDto, result);
        }
    }

    @Test
    @DisplayName("Should deactivate student when student active (Soft Delete)")
    void testDeleteStudentActive() {
        student.setActive(true);
        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        String result = studentService.deleteStudent(student.getId().toString());
        assertFalse(student.getActive());
        assertTrue(result.contains("is now not active"));
    }

    @Test
    @DisplayName("Should not deactivate when student not found")
    void testDeleteStudentInactive() {
        student.setActive(false);
        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        String result = studentService.deleteStudent(student.getId().toString());
        assertEquals("It wasn't possible to delete Student, check log", result);
    }

    @Test
    @DisplayName("Should throw exception when ID not found for deletion")
    void testDeleteStudentNotFound() {
        when(studentRepo.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> studentService.deleteStudent(UUID.randomUUID().toString()));
    }

    @Test
    @DisplayName("Should update student when success")
    void testUpdateStudent() {
        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        Parent parent = new Parent();
        Address address = new Address();
        try (var mockedParent = mockStatic(ParentMapper.class); var mockedAddress = mockStatic(AddressMapper.class); var mockedStudent = mockStatic(StudentMapper.class)) {
            mockedParent.when(() -> ParentMapper.toEntity(studentRequestDto.parent())).thenReturn(parent);
            mockedAddress.when(() -> AddressMapper.toEntity(studentRequestDto.address())).thenReturn(address);
            mockedStudent.when(() -> StudentMapper.toDto(any(Student.class))).thenReturn(studentReponseDto);
            when(studentRepo.save(any(Student.class))).thenReturn(student);
            StudentReponseDto result = studentService.updateStudent(student.getId().toString(), studentRequestDto);
            assertEquals(studentReponseDto, result);
        }
    }

    @Test
    @DisplayName("Should not update student when student not found")
    void testUpdateStudentNotFound() {
        when(studentRepo.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> studentService.updateStudent(UUID.randomUUID().toString(), studentRequestDto));
    }
}

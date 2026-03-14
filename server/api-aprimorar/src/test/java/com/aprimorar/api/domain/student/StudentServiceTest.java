package com.aprimorar.api.domain.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.ParentRepository;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.enums.BrazilianState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private static final UUID STUDENT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UUID PARENT_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

    @Mock
    private ParentRepository parentRepo;

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    @Nested
    @DisplayName("Command methods")
    class CommandMethods {

        @Test
        @DisplayName("should create student and persist new parent before student")
        void shouldCreateStudentAndPersistNewParentBeforeStudent() {
            StudentRequestDTO input = request(parent(null, "Ana Souza", "ana@email.com", "(11) 97777-6655", "123.456.789-01"));
            Parent transientParent = parent(null, "Ana Souza", "ana@email.com", "11977776655", "12345678901");
            Parent savedParent = parent(PARENT_ID, "Ana Souza", "ana@email.com", "11977776655", "12345678901");
            Student mappedStudent = student(transientParent);
            Student savedStudent = student(savedParent);
            savedStudent.setId(STUDENT_ID);
            StudentResponseDTO expected = response(savedStudent);

            when(studentMapper.convertToEntity(input)).thenReturn(mappedStudent);
            when(parentRepo.findByCpf("12345678901")).thenReturn(Optional.empty());
            when(parentRepo.existsByEmail("ana@email.com")).thenReturn(false);
            when(parentRepo.save(any(Parent.class))).thenReturn(savedParent);
            when(studentRepo.existsByCpf("12345678901")).thenReturn(false);
            when(studentRepo.existsByEmail("aluno@email.com")).thenReturn(false);
            when(studentRepo.save(mappedStudent)).thenReturn(savedStudent);
            when(studentMapper.convertToDto(savedStudent)).thenReturn(expected);

            StudentResponseDTO actual = studentService.createStudent(input);

            assertThat(actual).isEqualTo(expected);
            assertThat(mappedStudent.getParent()).isEqualTo(savedParent);
            verify(parentRepo).save(any(Parent.class));
            verify(studentRepo).save(mappedStudent);
        }

        @Test
        @DisplayName("should create student using existing parent when parent id is informed")
        void shouldCreateStudentUsingExistingParentWhenParentIdIsInformed() {
            StudentRequestDTO input = request(parent(PARENT_ID, "Ana Souza", "ana@email.com", "(11) 97777-6655", "123.456.789-01"));
            Parent detachedParent = parent(PARENT_ID, "Ana Souza", "ana@email.com", "11977776655", "12345678901");
            Parent existingParent = parent(PARENT_ID, "Ana Souza", "ana@email.com", "11977776655", "12345678901");
            Student mappedStudent = student(detachedParent);
            Student savedStudent = student(existingParent);
            savedStudent.setId(STUDENT_ID);
            StudentResponseDTO expected = response(savedStudent);

            when(studentMapper.convertToEntity(input)).thenReturn(mappedStudent);
            when(parentRepo.findById(PARENT_ID)).thenReturn(Optional.of(existingParent));
            when(studentRepo.existsByCpf("12345678901")).thenReturn(false);
            when(studentRepo.existsByEmail("aluno@email.com")).thenReturn(false);
            when(studentRepo.save(mappedStudent)).thenReturn(savedStudent);
            when(studentMapper.convertToDto(savedStudent)).thenReturn(expected);

            StudentResponseDTO actual = studentService.createStudent(input);

            assertThat(actual).isEqualTo(expected);
            assertThat(mappedStudent.getParent()).isEqualTo(existingParent);
            verify(parentRepo, never()).save(detachedParent);
            verify(studentRepo).save(mappedStudent);
        }
    }

    private static StudentRequestDTO request(Parent parent) {
        return new StudentRequestDTO(
                "Aluno Teste",
                LocalDate.of(2012, 5, 10),
                "123.456.789-01",
                "Escola Teste",
                "(11) 98888-7766",
                "aluno@email.com",
                address(),
                parent
        );
    }

    private static Student student(Parent parent) {
        Student student = new Student();
        student.setName("Aluno Teste");
        student.setBirthdate(LocalDate.of(2012, 5, 10));
        student.setCpf("12345678901");
        student.setSchool("Escola Teste");
        student.setContact("11988887766");
        student.setEmail("aluno@email.com");
        student.setAddress(address());
        student.setParent(parent);
        return student;
    }

    private static Parent parent(UUID id, String name, String email, String contact, String cpf) {
        Parent parent = new Parent();
        parent.setId(id);
        parent.setName(name);
        parent.setEmail(email);
        parent.setContact(contact);
        parent.setCpf(cpf);
        return parent;
    }

    private static Address address() {
        Address address = new Address();
        address.setStreet("Rua das Flores");
        address.setNumber("123");
        address.setDistrict("Centro");
        address.setCity("Sao Paulo");
        address.setState(BrazilianState.SP);
        address.setZip("04711230");
        address.setComplement("Casa");
        return address;
    }

    private static StudentResponseDTO response(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getContact(),
                student.getEmail(),
                student.getCpf(),
                student.getBirthdate(),
                student.getSchool(),
                12,
                student.getAddress(),
                student.getParent(),
                student.getArchivedAt(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}

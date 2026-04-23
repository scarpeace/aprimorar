package com.aprimorar.api.domain.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.address.dto.AddressRequestDTO;
import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.repository.ParentRepository;
import com.aprimorar.api.domain.student.dto.StudentOptionsDTO;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.StudentResponsibleSummaryDTO;
import com.aprimorar.api.domain.student.exception.StudentAlreadyExistException;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.enums.BrazilianStates;
import com.aprimorar.api.shared.PageDTO;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private static final UUID STUDENT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID SECOND_STUDENT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID PARENT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID MISSING_STUDENT_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final Instant CREATED_AT = Instant.parse("2026-01-05T08:00:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2026-01-12T12:30:00Z");
    private static final Instant ARCHIVED_AT = Instant.parse("2026-01-10T10:15:30Z");

    @Mock
    private ParentRepository parentRepo;

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private EventRepository eventRepo;

    @InjectMocks
    private StudentService studentService;

    @Nested
    @DisplayName("Command methods")
    class CommandMethods {

        @Test
        @DisplayName("should create student when input is valid")
        void shouldCreateStudentWhenInputIsValid() {
            StudentRequestDTO input = request();
            Parent parent = parent();
            Address address = address();
            Student savedStudent = student();
            StudentResponseDTO expected = response();

            when(parentRepo.findById(PARENT_ID)).thenReturn(Optional.of(parent));
            when(addressMapper.convertToEntity(input.address())).thenReturn(address);
            when(studentRepo.existsByCpf("12345678901")).thenReturn(false);
            when(studentRepo.existsByEmail("joao@email.com")).thenReturn(false);
            when(studentRepo.save(any(Student.class))).thenReturn(savedStudent);
            when(studentMapper.convertToDto(savedStudent)).thenReturn(expected);

            StudentResponseDTO actual = studentService.createStudent(input);

            assertThat(actual).isEqualTo(expected);
            verify(studentRepo).existsByCpf("12345678901");
            verify(studentRepo).existsByEmail("joao@email.com");
            verify(studentRepo).save(any(Student.class));
        }

        @Test
        @DisplayName("should throw when creating student with duplicated cpf")
        void shouldThrowWhenCreatingStudentWithDuplicatedCpf() {
            StudentRequestDTO input = request();

            when(parentRepo.findById(PARENT_ID)).thenReturn(Optional.of(parent()));
            when(addressMapper.convertToEntity(input.address())).thenReturn(address());
            when(studentRepo.existsByCpf("12345678901")).thenReturn(true);

            assertThatThrownBy(() -> studentService.createStudent(input))
                .isInstanceOf(StudentAlreadyExistException.class)
                .hasMessage("Aluno com o CPF informado já existe no banco de dados");

            verify(studentRepo).existsByCpf("12345678901");
            verify(studentRepo, never()).existsByEmail(any());
            verify(studentRepo, never()).save(any());
        }

        @Test
        @DisplayName("should throw when creating student with duplicated email")
        void shouldThrowWhenCreatingStudentWithDuplicatedEmail() {
            StudentRequestDTO input = request();

            when(parentRepo.findById(PARENT_ID)).thenReturn(Optional.of(parent()));
            when(addressMapper.convertToEntity(input.address())).thenReturn(address());
            when(studentRepo.existsByCpf("12345678901")).thenReturn(false);
            when(studentRepo.existsByEmail("joao@email.com")).thenReturn(true);

            assertThatThrownBy(() -> studentService.createStudent(input))
                .isInstanceOf(StudentAlreadyExistException.class)
                .hasMessage("Aluno com o Email informado já existe no banco de dados");

            verify(studentRepo).existsByCpf("12345678901");
            verify(studentRepo).existsByEmail("joao@email.com");
            verify(studentRepo, never()).save(any());
        }

        @Test
        @DisplayName("should throw when parent is not found during creation")
        void shouldThrowWhenParentIsNotFoundDuringCreation() {
            StudentRequestDTO input = request();

            when(parentRepo.findById(PARENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> studentService.createStudent(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Responsável não encontrado.");

            verify(addressMapper, never()).convertToEntity(any());
            verify(studentRepo, never()).save(any());
        }

        @Test
        @DisplayName("should reject creating student without responsible id")
        void shouldRejectCreatingStudentWithoutResponsibleId() {
            StudentRequestDTO input = new StudentRequestDTO(
                "João Silva",
                LocalDate.of(2010, 5, 10),
                "123.456.789-01",
                "Escola Central",
                "(61) 99999-9999",
                "joao@email.com",
                addressRequest(),
                null
            );

            assertThatThrownBy(() -> studentService.createStudent(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Responsável do aluno é obrigatório.");

            verify(addressMapper, never()).convertToEntity(any());
            verify(studentRepo, never()).save(any());
        }

        @Test
        @DisplayName("should update student when input is valid")
        void shouldUpdateStudentWhenInputIsValid() {
            StudentRequestDTO input = updatedRequest();
            Student existingStudent = student();
            Parent parent = secondParent();
            Address address = secondAddress();
            StudentResponseDTO expected = updatedResponse();

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(existingStudent));
            when(parentRepo.findById(PARENT_ID)).thenReturn(Optional.of(parent));
            when(addressMapper.convertToEntity(input.address())).thenReturn(address);
            when(studentRepo.existsByEmailAndIdNot("joao.pedro@email.com", STUDENT_ID)).thenReturn(false);
            when(studentMapper.convertToDto(existingStudent)).thenReturn(expected);

            StudentResponseDTO actual = studentService.updateStudent(input, STUDENT_ID);

            assertThat(actual).isEqualTo(expected);
            assertThat(existingStudent.getName()).isEqualTo("João Pedro");
            assertThat(existingStudent.getContact()).isEqualTo("11988887777");
            assertThat(existingStudent.getEmail()).isEqualTo("joao.pedro@email.com");
            assertThat(existingStudent.getBirthdate()).isEqualTo(LocalDate.of(2011, 6, 15));
            assertThat(existingStudent.getSchool()).isEqualTo("Nova Escola");
            assertThat(existingStudent.getParent()).isEqualTo(parent);
            assertThat(existingStudent.getAddress()).isEqualTo(address);
            assertThat(existingStudent.getCpf()).isEqualTo("12345678901");
            verify(studentRepo).existsByEmailAndIdNot("joao.pedro@email.com", STUDENT_ID);
        }

        @Test
        @DisplayName("should throw when updating student with duplicated email")
        void shouldThrowWhenUpdatingStudentWithDuplicatedEmail() {
            StudentRequestDTO input = updatedRequest();
            Student existingStudent = student();

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(existingStudent));
            when(parentRepo.findById(PARENT_ID)).thenReturn(Optional.of(secondParent()));
            when(addressMapper.convertToEntity(input.address())).thenReturn(secondAddress());
            when(studentRepo.existsByEmailAndIdNot("joao.pedro@email.com", STUDENT_ID)).thenReturn(true);

            assertThatThrownBy(() -> studentService.updateStudent(input, STUDENT_ID))
                .isInstanceOf(StudentAlreadyExistException.class)
                .hasMessage("Aluno com o Email informado já existe no banco de dados");

            verify(studentMapper, never()).convertToDto(any());
        }

        @Test
        @DisplayName("should throw when updating student that does not exist")
        void shouldThrowWhenUpdatingStudentThatDoesNotExist() {
            when(studentRepo.findById(MISSING_STUDENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> studentService.updateStudent(updatedRequest(), MISSING_STUDENT_ID))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("Aluno não encontrado no banco de dados");

            verify(parentRepo, never()).findById(any());
            verify(studentMapper, never()).convertToDto(any());
        }

        @Test
        @DisplayName("should reject updating student without responsible id")
        void shouldRejectUpdatingStudentWithoutResponsibleId() {
            StudentRequestDTO input = new StudentRequestDTO(
                "João Pedro",
                LocalDate.of(2011, 6, 15),
                "999.999.999-99",
                "Nova Escola",
                "(11) 98888-7777",
                "joao.pedro@email.com",
                secondAddressRequest(),
                null
            );

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student()));

            assertThatThrownBy(() -> studentService.updateStudent(input, STUDENT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Responsável do aluno é obrigatório.");

            verify(studentMapper, never()).convertToDto(any());
        }

        @Test
        @DisplayName("should throw when updating ghost student")
        void shouldThrowWhenUpdatingGhostStudent() {
            assertThatThrownBy(() -> studentService.updateStudent(updatedRequest(), GHOST_STUDENT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Não é possível modificar o registro de sistema 'Aluno Removido'.");

            verify(studentRepo, never()).findById(any());
        }

        @Test
        @DisplayName("should archive student by setting archivedAt")
        void shouldArchiveStudentBySettingArchivedAt() {
            Student existingStudent = student();

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(existingStudent));

            studentService.archiveStudent(STUDENT_ID);

            assertThat(existingStudent.getArchivedAt()).isNotNull();
        }

        @Test
        @DisplayName("should unarchive student by clearing archivedAt")
        void shouldUnarchiveStudentByClearingArchivedAt() {
            Student existingStudent = student();
            existingStudent.setArchivedAt(ARCHIVED_AT);

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(existingStudent));

            studentService.unarchiveStudent(STUDENT_ID);

            assertThat(existingStudent.getArchivedAt()).isNull();
        }

        @Test
        @DisplayName("should delete student and reassign events to ghost")
        void shouldDeleteStudentAndReassignEventsToGhost() {
            Student existingStudent = student();

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(existingStudent));

            studentService.deleteStudent(STUDENT_ID);

            verify(eventRepo).reassignEventsToGhost(STUDENT_ID, GHOST_STUDENT_ID);
            verify(studentRepo).delete(existingStudent);
        }
    }

    @Nested
    @DisplayName("Query methods")
    class QueryMethods {

        @Test
        @DisplayName("should return paged students")
        void shouldReturnPagedStudents() {
            Pageable input = PageRequest.of(0, 2);
            Student firstStudent = student();
            Student secondStudent = secondStudent();
            StudentResponseDTO expectedFirst = response();
            StudentResponseDTO expectedSecond = secondResponse();
            Page<Student> expectedPage = new PageImpl<>(List.of(firstStudent, secondStudent), input, 2);

            when(studentRepo.findAll(any(Specification.class), eq(input))).thenReturn(expectedPage);
            when(studentMapper.convertToDto(firstStudent)).thenReturn(expectedFirst);
            when(studentMapper.convertToDto(secondStudent)).thenReturn(expectedSecond);

            PageDTO<StudentResponseDTO> actual = studentService.getStudents(input, null, false);

            assertThat(actual.content()).containsExactly(expectedFirst, expectedSecond);
            assertThat(actual.totalElements()).isEqualTo(2);
        }

        @Test
        @DisplayName("should exclude archived students by default when archived filter is false")
        void shouldExcludeArchivedStudentsByDefaultWhenArchivedFilterIsFalse() {
            Pageable input = PageRequest.of(0, 2);
            Page<Student> expectedPage = new PageImpl<>(List.of(student()), input, 1);

            when(studentRepo.findAll(any(Specification.class), eq(input))).thenReturn(expectedPage);
            when(studentMapper.convertToDto(any(Student.class))).thenReturn(response());

            studentService.getStudents(input, null, false);

            Specification<Student> specification = captureStudentListSpecification(input);
            assertUsesArchivedFlag(specification, false);
        }

        @Test
        @DisplayName("should exclude archived students by default when archived filter is omitted")
        void shouldExcludeArchivedStudentsByDefaultWhenArchivedFilterIsOmitted() {
            Pageable input = PageRequest.of(0, 2);
            Page<Student> expectedPage = new PageImpl<>(List.of(student()), input, 1);

            when(studentRepo.findAll(any(Specification.class), eq(input))).thenReturn(expectedPage);
            when(studentMapper.convertToDto(any(Student.class))).thenReturn(response());

            studentService.getStudents(input, null, null);

            Specification<Student> specification = captureStudentListSpecification(input);
            assertUsesArchivedFlag(specification, false);
        }

        @Test
        @DisplayName("should return archived students when archived filter is true")
        void shouldReturnArchivedStudentsWhenArchivedFilterIsTrue() {
            Pageable input = PageRequest.of(0, 2);
            Student archivedStudent = student();
            archivedStudent.setArchivedAt(ARCHIVED_AT);
            Page<Student> expectedPage = new PageImpl<>(List.of(archivedStudent), input, 1);

            when(studentRepo.findAll(any(Specification.class), eq(input))).thenReturn(expectedPage);
            when(studentMapper.convertToDto(any(Student.class))).thenReturn(response());

            studentService.getStudents(input, null, true);

            Specification<Student> specification = captureStudentListSpecification(input);
            assertUsesArchivedFlag(specification, true);
        }

        @Test
        @DisplayName("should return student by id")
        void shouldReturnStudentById() {
            Student input = student();
            StudentResponseDTO expected = response();

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(input));
            when(studentMapper.convertToDto(input)).thenReturn(expected);

            StudentResponseDTO actual = studentService.findById(STUDENT_ID);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("should throw when student is not found by id")
        void shouldThrowWhenStudentIsNotFoundById() {
            when(studentRepo.findById(MISSING_STUDENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> studentService.findById(MISSING_STUDENT_ID))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("Aluno não encontrado no banco de dados");
        }

        @Test
        @DisplayName("should return student options")
        void shouldReturnStudentOptions() {
            Student firstStudent = student();
            Student secondStudent = secondStudent();

            when(studentRepo.findAll(any(Specification.class), eq(Sort.by(Sort.Direction.ASC, "name"))))
                .thenReturn(List.of(firstStudent, secondStudent));

            List<StudentOptionsDTO> actual = studentService.getStudentOptions();

            assertThat(actual)
                .containsExactly(
                    new StudentOptionsDTO(STUDENT_ID, "João Silva"),
                    new StudentOptionsDTO(SECOND_STUDENT_ID, "Maria Eduarda")
                );
        }

        @Test
        @DisplayName("should return students by parent")
        void shouldReturnStudentsByParent() {
            Pageable input = PageRequest.of(0, 2);
            Student firstStudent = student();
            Student secondStudent = secondStudent();
            StudentResponseDTO expectedFirst = response();
            StudentResponseDTO expectedSecond = secondResponse();
            Page<Student> expectedPage = new PageImpl<>(List.of(firstStudent, secondStudent), input, 2);

            when(studentRepo.findAllByParentId(PARENT_ID, input)).thenReturn(expectedPage);
            when(studentMapper.convertToDto(firstStudent)).thenReturn(expectedFirst);
            when(studentMapper.convertToDto(secondStudent)).thenReturn(expectedSecond);

            PageDTO<StudentResponseDTO> actual = studentService.getStudentsByParent(PARENT_ID, input);

            assertThat(actual.content()).containsExactly(expectedFirst, expectedSecond);
            assertThat(actual.totalElements()).isEqualTo(2);
        }

        @Test
        @DisplayName("should expose responsible summary in student read contract")
        void shouldExposeResponsibleSummaryInStudentReadContract() {
            assertThat(
                Arrays.stream(StudentResponseDTO.class.getRecordComponents())
                    .anyMatch(component -> component.getName().equals("responsible"))
            ).isTrue();
        }
    }

    private static StudentRequestDTO request() {
        return new StudentRequestDTO(
            "João Silva",
            LocalDate.of(2010, 5, 10),
            "123.456.789-01",
            "Escola Central",
            "(61) 99999-9999",
            "joao@email.com",
            addressRequest(),
            PARENT_ID
        );
    }

    private static StudentRequestDTO updatedRequest() {
        return new StudentRequestDTO(
            "João Pedro",
            LocalDate.of(2011, 6, 15),
            "999.999.999-99",
            "Nova Escola",
            "(11) 98888-7777",
            "joao.pedro@email.com",
            secondAddressRequest(),
            PARENT_ID
        );
    }

    private static AddressRequestDTO addressRequest() {
        return new AddressRequestDTO("Rua A", "Centro", "Brasília", BrazilianStates.DF, "70000000", "Casa");
    }

    private static AddressRequestDTO secondAddressRequest() {
        return new AddressRequestDTO("Rua B", "Asa Sul", "Brasília", BrazilianStates.DF, "70200000", "Apto 101");
    }

    private static Parent parent() {
        Parent input = new Parent("Maria Silva", "maria@email.com", "61977777777", "98765432100");
        input.setId(PARENT_ID);
        return input;
    }

    private static Parent secondParent() {
        Parent input = new Parent("Carlos Silva", "carlos@email.com", "61988888888", "98765432100");
        input.setId(PARENT_ID);
        return input;
    }

    private static Address address() {
        Address input = new Address();
        input.setStreet("Rua A");
        input.setDistrict("Centro");
        input.setCity("Brasília");
        input.setState(BrazilianStates.DF);
        input.setZip("70000000");
        input.setComplement("Casa");
        return input;
    }

    private static Address secondAddress() {
        Address input = new Address();
        input.setStreet("Rua B");
        input.setDistrict("Asa Sul");
        input.setCity("Brasília");
        input.setState(BrazilianStates.DF);
        input.setZip("70200000");
        input.setComplement("Apto 101");
        return input;
    }

    private static Student student() {
        Student input = new Student(
            "João Silva",
            "61999999999",
            "joao@email.com",
            LocalDate.of(2010, 5, 10),
            "12345678901",
            "Escola Central",
            parent(),
            address()
        );
        input.setId(STUDENT_ID);
        input.setCreatedAt(CREATED_AT);
        input.setUpdatedAt(UPDATED_AT);
        return input;
    }

    private static Student secondStudent() {
        Student input = new Student(
            "Maria Eduarda",
            "61988887777",
            "maria@email.com",
            LocalDate.of(2012, 7, 20),
            "98765432100",
            "Colégio Brasil",
            parent(),
            secondAddress()
        );
        input.setId(SECOND_STUDENT_ID);
        input.setCreatedAt(CREATED_AT);
        input.setUpdatedAt(UPDATED_AT);
        return input;
    }

    private static StudentResponseDTO response() {
        return new StudentResponseDTO(
            STUDENT_ID,
            "João Silva",
            "61999999999",
            "joao@email.com",
            "12345678901",
            LocalDate.of(2010, 5, 10),
            "Escola Central",
            15,
            new AddressResponseDTO("Rua A", "Centro", "Brasília", BrazilianStates.DF, "70000-000", "Casa"),
            PARENT_ID,
            responsibleSummary(),
            null,
            UPDATED_AT,
            CREATED_AT
        );
    }

    private static StudentResponseDTO updatedResponse() {
        return new StudentResponseDTO(
            STUDENT_ID,
            "João Pedro",
            "11988887777",
            "joao.pedro@email.com",
            "12345678901",
            LocalDate.of(2011, 6, 15),
            "Nova Escola",
            14,
            new AddressResponseDTO("Rua B", "Asa Sul", "Brasília", BrazilianStates.DF, "70200-000", "Apto 101"),
            PARENT_ID,
            responsibleSummary(),
            null,
            UPDATED_AT,
            CREATED_AT
        );
    }

    private static StudentResponseDTO secondResponse() {
        return new StudentResponseDTO(
            SECOND_STUDENT_ID,
            "Maria Eduarda",
            "61988887777",
            "maria@email.com",
            "98765432100",
            LocalDate.of(2012, 7, 20),
            "Colégio Brasil",
            13,
            new AddressResponseDTO("Rua B", "Asa Sul", "Brasília", BrazilianStates.DF, "70200-000", "Apto 101"),
            PARENT_ID,
            responsibleSummary(),
            null,
            UPDATED_AT,
            CREATED_AT
        );
    }

    private static StudentResponsibleSummaryDTO responsibleSummary() {
        return new StudentResponsibleSummaryDTO(PARENT_ID, "Maria Silva", "61977777777", "98765432100");
    }

    @SuppressWarnings("unchecked")
    private Specification<Student> captureStudentListSpecification(Pageable pageable) {
        ArgumentCaptor<Specification<Student>> specificationCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(studentRepo).findAll(specificationCaptor.capture(), eq(pageable));
        return specificationCaptor.getValue();
    }

    @SuppressWarnings("unchecked")
    private void assertUsesArchivedFlag(Specification<Student> specification, boolean archivedOnly) {
        Root<Student> root = org.mockito.Mockito.mock(Root.class);
        CriteriaQuery<?> query = org.mockito.Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = org.mockito.Mockito.mock(CriteriaBuilder.class);
        Path<Object> idPath = org.mockito.Mockito.mock(Path.class);
        Path<Instant> archivedAtPath = org.mockito.Mockito.mock(Path.class);
        when(root.get("id")).thenReturn(idPath);
        when(root.<Instant>get("archivedAt")).thenReturn(archivedAtPath);
        when(criteriaBuilder.notEqual(idPath, GHOST_STUDENT_ID)).thenReturn(org.mockito.Mockito.mock(jakarta.persistence.criteria.Predicate.class));

        if (archivedOnly) {
            when(criteriaBuilder.isNotNull(archivedAtPath)).thenReturn(org.mockito.Mockito.mock(jakarta.persistence.criteria.Predicate.class));
        } else {
            when(criteriaBuilder.isNull(archivedAtPath)).thenReturn(org.mockito.Mockito.mock(jakarta.persistence.criteria.Predicate.class));
        }

        specification.toPredicate(root, query, criteriaBuilder);

        if (archivedOnly) {
            verify(criteriaBuilder).isNotNull(archivedAtPath);
        } else {
            verify(criteriaBuilder).isNull(archivedAtPath);
        }
    }
}

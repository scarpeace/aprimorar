package com.aprimorar.api.domain.parent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;
import com.aprimorar.api.domain.parent.repository.ParentRepository;
import com.aprimorar.api.shared.PageDTO;

@ExtendWith(MockitoExtension.class)
class ParentServiceTest {

    private static final UUID EXISTING_PARENT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID SECOND_PARENT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID MISSING_PARENT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final Instant EXISTING_ARCHIVED_AT = Instant.parse("2026-01-10T10:15:30Z");
    private static final Instant EXISTING_CREATED_AT = Instant.parse("2026-01-05T08:00:00Z");
    private static final Instant EXISTING_UPDATED_AT = Instant.parse("2026-01-12T12:30:00Z");

    @Mock
    private ParentRepository parentRepo;

    @Mock
    private ParentMapper parentMapper;

    @Mock
    private com.aprimorar.api.domain.student.repository.StudentRepository studentRepo;

    @InjectMocks
    private ParentService parentService;

//TODO: voltar aqui e arrumar esse teste
    @Nested
    @DisplayName("Query methods")
    class QueryMethods {

        // @Test
        // @DisplayName("should return paged parents using deterministic input and expected values")
        // void shouldReturnPagedParents() {
        //     // Arrange
        //     Pageable input = PageRequest.of(0, 2);
        //     Parent firstParent = parent(EXISTING_PARENT_ID, "Maria Souza", "maria@email.com", "61999998888", "12345678901");
        //     Parent secondParent = parent(SECOND_PARENT_ID, "Carlos Lima", "carlos@email.com", "61999997777", "98765432100");
        //     Page<Parent> expectedPage = new PageImpl<>(List.of(firstParent, secondParent), input, 2);
        //     ParentResponseDTO expectedFirst = response(EXISTING_PARENT_ID, "Maria Souza", "maria@email.com", "(61) 99999-8888", "123.456.789-01");
        //     ParentResponseDTO expectedSecond = response(SECOND_PARENT_ID, "Carlos Lima", "carlos@email.com", "(61) 99999-7777", "987.654.321-00");

        //     when(parentRepo.findAll(input)).thenReturn(expectedPage);
        //     when(parentMapper.convertToDto(firstParent)).thenReturn(expectedFirst);
        //     when(parentMapper.convertToDto(secondParent)).thenReturn(expectedSecond);

        //     // Act
        //     PageDTO<ParentResponseDTO> actual = parentService.getParents(input, null);

        //     // Assert
        //     assertThat(actual.getContent()).containsExactly(expectedFirst, expectedSecond);
        //     assertThat(actual.getTotalElements()).isEqualTo(2);
        //     verify(parentRepo).findAll(input);
        //     verify(parentMapper).convertToDto(firstParent);
        //     verify(parentMapper).convertToDto(secondParent);
        // }

        @Test
        @DisplayName("should return parent by id when parent exists")
        void shouldReturnParentById() {
            // Arrange
            UUID id = EXISTING_PARENT_ID;
            Parent input = parent(id, "Maria Souza", "maria@email.com", "61999998888", "12345678901");
            ParentResponseDTO expected = response(id, "Maria Souza", "maria@email.com", "(61) 99999-8888", "123.456.789-01");

            when(parentRepo.findById(id)).thenReturn(Optional.of(input));
            when(parentMapper.convertToDto(input)).thenReturn(expected);

            // Act
            ParentResponseDTO actual = parentService.findById(id);

            // Assert
            assertThat(actual).isEqualTo(expected);
            verify(parentRepo).findById(id);
            verify(parentMapper).convertToDto(input);
        }

        @Test
        @DisplayName("should throw when parent is not found by id")
        void shouldThrowWhenParentIsNotFoundById() {
            // Arrange
            UUID input = MISSING_PARENT_ID;

            when(parentRepo.findById(input)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> parentService.findById(input))
                    .isInstanceOf(ParentNotFoundException.class)
                    .hasMessage("Responsável não encontrado no banco de dados");

            verify(parentRepo).findById(input);
        }
    }

    @Nested
    @DisplayName("Command methods")
    class CommandMethods {

        @Test
        @DisplayName("should create parent when cpf and email are unique")
        void shouldCreateParentWhenCpfAndEmailAreUnique() {
            // Arrange
            ParentRequestDTO input = request("Maria Souza", "maria@email.com", "(61) 99999-8888", "123.456.789-01");
            Parent mappedParent = parent(EXISTING_PARENT_ID, "Maria Souza", "maria@email.com", "61999998888", "12345678901");
            ParentResponseDTO expected = response(EXISTING_PARENT_ID, "Maria Souza", "maria@email.com", "(61) 99999-8888", "123.456.789-01");

            when(parentMapper.convertToEntity(input)).thenReturn(mappedParent);
            when(parentRepo.existsByCpf("123.456.789-01")).thenReturn(false);
            when(parentRepo.existsByEmail("maria@email.com")).thenReturn(false);
            when(parentRepo.save(mappedParent)).thenReturn(mappedParent);
            when(parentMapper.convertToDto(mappedParent)).thenReturn(expected);

            // Act
            ParentResponseDTO actual = parentService.createParent(input);

            // Assert
            assertThat(actual).isEqualTo(expected);
            verify(parentMapper).convertToEntity(input);
            verify(parentRepo).existsByCpf("123.456.789-01");
            verify(parentRepo).existsByEmail("maria@email.com");
            verify(parentRepo).save(mappedParent);
            verify(parentMapper).convertToDto(mappedParent);
        }

        @Test
        @DisplayName("should throw when creating parent with duplicated cpf")
        void shouldThrowWhenCreatingParentWithDuplicatedCpf() {
            // Arrange
            ParentRequestDTO input = request("Maria Souza", "maria@email.com", "(61) 99999-8888", "123.456.789-01");
            Parent mappedParent = parent(EXISTING_PARENT_ID, "Maria Souza", "maria@email.com", "61999998888", "12345678901");

            when(parentMapper.convertToEntity(input)).thenReturn(mappedParent);
            when(parentRepo.existsByCpf("123.456.789-01")).thenReturn(true);

            // Act + Assert
            assertThatThrownBy(() -> parentService.createParent(input))
                    .isInstanceOf(ParentAlreadyExistsException.class)
                    .hasMessage("Responsável com o CPF informado já existe no banco de dados");

            verify(parentMapper).convertToEntity(input);
            verify(parentRepo).existsByCpf("123.456.789-01");
            verify(parentRepo).existsByEmail("maria@email.com");
            verify(parentRepo, never()).save(any());
        }

        @Test
        @DisplayName("should throw when creating parent with duplicated email")
        void shouldThrowWhenCreatingParentWithDuplicatedEmail() {
            // Arrange
            ParentRequestDTO input = request("Maria Souza", "maria@email.com", "(61) 99999-8888", "123.456.789-01");
            Parent mappedParent = parent(EXISTING_PARENT_ID, "Maria Souza", "maria@email.com", "61999998888", "12345678901");

            when(parentMapper.convertToEntity(input)).thenReturn(mappedParent);
            when(parentRepo.existsByCpf("123.456.789-01")).thenReturn(false);
            when(parentRepo.existsByEmail("maria@email.com")).thenReturn(true);

            // Act + Assert
            assertThatThrownBy(() -> parentService.createParent(input))
                    .isInstanceOf(ParentAlreadyExistsException.class)
                    .hasMessage("Responsável com o Email informado já existe no banco de dados");

            verify(parentMapper).convertToEntity(input);
            verify(parentRepo).existsByCpf("123.456.789-01");
            verify(parentRepo).existsByEmail("maria@email.com");
            verify(parentRepo, never()).save(any());
        }

        @Test
        @DisplayName("should update parent when cpf and email are unique for another record")
        void shouldUpdateParentWhenCpfAndEmailAreUniqueForAnotherRecord() {
            // Arrange
            UUID inputId = EXISTING_PARENT_ID;
            ParentRequestDTO input = request("Maria Souza Atualizada", "maria.atualizada@email.com", "(11) 98888-7777", "987.654.321-00");
            Parent mappedParent = parent(SECOND_PARENT_ID, "Maria Souza Atualizada", "maria.atualizada@email.com", "11988887777", "98765432100");
            Parent existingParent = parent(inputId, "Maria Souza", "maria@email.com", "61999998888", "12345678901");
            ParentResponseDTO expected = response(inputId, "Maria Souza Atualizada", "maria.atualizada@email.com", "(11) 98888-7777", "987.654.321-00");

            when(parentMapper.convertToEntity(input)).thenReturn(mappedParent);
            when(parentRepo.findById(inputId)).thenReturn(Optional.of(existingParent));
            when(parentRepo.existsByCpfAndIdNot("98765432100", inputId)).thenReturn(false);
            when(parentRepo.existsByEmailAndIdNot("maria.atualizada@email.com", inputId)).thenReturn(false);
            when(parentMapper.convertToDto(existingParent)).thenReturn(expected);

            // Act
            ParentResponseDTO actual = parentService.updateParent(inputId, input);

            // Assert
            assertThat(actual).isEqualTo(expected);
            assertThat(existingParent.getName()).isEqualTo("Maria Souza Atualizada");
            assertThat(existingParent.getEmail()).isEqualTo("maria.atualizada@email.com");
            assertThat(existingParent.getContact()).isEqualTo("11988887777");
            assertThat(existingParent.getCpf()).isEqualTo("98765432100");
            verify(parentMapper).convertToEntity(input);
            verify(parentRepo).findById(inputId);
            verify(parentRepo).existsByCpfAndIdNot("98765432100", inputId);
            verify(parentRepo).existsByEmailAndIdNot("maria.atualizada@email.com", inputId);
            verify(parentMapper).convertToDto(existingParent);
            verify(parentRepo, never()).save(any());
        }

        @Test
        @DisplayName("should throw when updating parent with duplicated cpf")
        void shouldThrowWhenUpdatingParentWithDuplicatedCpf() {
            // Arrange
            UUID inputId = EXISTING_PARENT_ID;
            ParentRequestDTO input = request("Maria Souza Atualizada", "maria.atualizada@email.com", "(11) 98888-7777", "987.654.321-00");
            Parent mappedParent = parent(SECOND_PARENT_ID, "Maria Souza Atualizada", "maria.atualizada@email.com", "11988887777", "98765432100");
            Parent existingParent = parent(inputId, "Maria Souza", "maria@email.com", "61999998888", "12345678901");

            when(parentMapper.convertToEntity(input)).thenReturn(mappedParent);
            when(parentRepo.findById(inputId)).thenReturn(Optional.of(existingParent));
            when(parentRepo.existsByCpfAndIdNot("98765432100", inputId)).thenReturn(true);

            // Act + Assert
            assertThatThrownBy(() -> parentService.updateParent(inputId, input))
                    .isInstanceOf(ParentAlreadyExistsException.class)
                    .hasMessage("Responsável com o CPF informado já existe no banco de dados");

            verify(parentRepo).findById(inputId);
            verify(parentRepo).existsByCpfAndIdNot("98765432100", inputId);
            verify(parentRepo, never()).existsByEmailAndIdNot(any(), any());
            verify(parentMapper, never()).convertToDto(any());
        }

        @Test
        @DisplayName("should throw when updating parent with duplicated email")
        void shouldThrowWhenUpdatingParentWithDuplicatedEmail() {
            // Arrange
            UUID inputId = EXISTING_PARENT_ID;
            ParentRequestDTO input = request("Maria Souza Atualizada", "maria.atualizada@email.com", "(11) 98888-7777", "987.654.321-00");
            Parent mappedParent = parent(SECOND_PARENT_ID, "Maria Souza Atualizada", "maria.atualizada@email.com", "11988887777", "98765432100");
            Parent existingParent = parent(inputId, "Maria Souza", "maria@email.com", "61999998888", "12345678901");

            when(parentMapper.convertToEntity(input)).thenReturn(mappedParent);
            when(parentRepo.findById(inputId)).thenReturn(Optional.of(existingParent));
            when(parentRepo.existsByCpfAndIdNot("98765432100", inputId)).thenReturn(false);
            when(parentRepo.existsByEmailAndIdNot("maria.atualizada@email.com", inputId)).thenReturn(true);

            // Act + Assert
            assertThatThrownBy(() -> parentService.updateParent(inputId, input))
                    .isInstanceOf(ParentAlreadyExistsException.class)
                    .hasMessage("Responsável com o Email informado já existe no banco de dados");

            verify(parentRepo).findById(inputId);
            verify(parentRepo).existsByCpfAndIdNot("98765432100", inputId);
            verify(parentRepo).existsByEmailAndIdNot("maria.atualizada@email.com", inputId);
            verify(parentMapper, never()).convertToDto(any());
        }

        @Test
        @DisplayName("should throw when updating a parent that does not exist")
        void shouldThrowWhenUpdatingParentThatDoesNotExist() {
            // Arrange
            UUID inputId = MISSING_PARENT_ID;
            ParentRequestDTO input = request("Maria Souza Atualizada", "maria.atualizada@email.com", "(11) 98888-7777", "987.654.321-00");
            Parent mappedParent = parent(SECOND_PARENT_ID, "Maria Souza Atualizada", "maria.atualizada@email.com", "11988887777", "98765432100");

            when(parentMapper.convertToEntity(input)).thenReturn(mappedParent);
            when(parentRepo.findById(inputId)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> parentService.updateParent(inputId, input))
                    .isInstanceOf(ParentNotFoundException.class)
                    .hasMessage("Responsável não encontrado no banco de dados");

            verify(parentMapper).convertToEntity(input);
            verify(parentRepo).findById(inputId);
            verify(parentRepo, never()).existsByCpfAndIdNot(any(), any());
            verify(parentMapper, never()).convertToDto(any());
        }

        @Test
        @DisplayName("should archive parent by setting archivedAt")
        void shouldArchiveParentBySettingArchivedAt() {
            // Arrange
            UUID input = EXISTING_PARENT_ID;
            Parent expected = parent(input, "Maria Souza", "maria@email.com", "61999998888", "12345678901");

            when(parentRepo.findById(input)).thenReturn(Optional.of(expected));

            // Act
            parentService.archiveParent(input);

            // Assert
            assertThat(expected.getArchivedAt()).isNotNull();
            verify(parentRepo).findById(input);
        }

        @Test
        @DisplayName("should unarchive parent by clearing archivedAt")
        void shouldUnarchiveParentByClearingArchivedAt() {
            // Arrange
            UUID input = EXISTING_PARENT_ID;
            Parent expected = parent(input, "Maria Souza", "maria@email.com", "61999998888", "12345678901");
            expected.setArchivedAt(EXISTING_ARCHIVED_AT);

            when(parentRepo.findById(input)).thenReturn(Optional.of(expected));

            // Act
            parentService.unarchiveParent(input);

            // Assert
            assertThat(expected.getArchivedAt()).isNull();
            verify(parentRepo).findById(input);
        }

        @Test
        @DisplayName("should delete parent when it exists")
        void shouldDeleteParentWhenItExists() {
            // Arrange
            UUID input = EXISTING_PARENT_ID;
            Parent expected = parent(input, "Maria Souza", "maria@email.com", "61999998888", "12345678901");

            when(parentRepo.findById(input)).thenReturn(Optional.of(expected));
            when(studentRepo.existsByParentId(input)).thenReturn(false);

            // Act
            parentService.deleteParent(input);

            // Assert
            verify(parentRepo).findById(input);
            verify(studentRepo).existsByParentId(input);
            verify((org.springframework.data.repository.CrudRepository<Parent, java.util.UUID>) parentRepo).delete((Parent) expected);
        }

        @Test
        @DisplayName("should throw when deleting parent that does not exist")
        void shouldThrowWhenDeletingParentThatDoesNotExist() {
            // Arrange
            UUID input = MISSING_PARENT_ID;

            when(parentRepo.findById(input)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> parentService.deleteParent(input))
                    .isInstanceOf(ParentNotFoundException.class)
                    .hasMessage("Responsável não encontrado no banco de dados");

            verify(parentRepo).findById(input);
            verify((org.springframework.data.repository.CrudRepository<Parent, java.util.UUID>) parentRepo, never()).delete(any());
        }
    }

    private static ParentRequestDTO request(String name, String email, String contact, String cpf) {
        return new ParentRequestDTO(name, email, contact, cpf);
    }

    private static ParentResponseDTO response(UUID id, String name, String email, String contact, String cpf) {
        return new ParentResponseDTO(id, name, email, contact, cpf, null, EXISTING_CREATED_AT, EXISTING_UPDATED_AT);
    }

    private static Parent parent(UUID id, String name, String email, String contact, String cpf) {
        Parent parent = new Parent();
        parent.setId(id);
        parent.setName(name);
        parent.setEmail(email);
        parent.setContact(contact);
        parent.setCpf(cpf);
        parent.setCreatedAt(EXISTING_CREATED_AT);
        parent.setUpdatedAt(EXISTING_UPDATED_AT);
        return parent;
    }
}

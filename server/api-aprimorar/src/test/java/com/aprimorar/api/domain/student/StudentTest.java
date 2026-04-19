package com.aprimorar.api.domain.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.domain.student.exception.InvalidStudentException;
import com.aprimorar.api.enums.BrazilianStates;
import java.time.LocalDate;
import java.lang.reflect.Method;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    @DisplayName("should mark student read queries to fetch responsável explicitly")
    void shouldMarkStudentReadQueriesToFetchResponsavelExplicitly() throws NoSuchMethodException {
        Method findById = StudentRepository.class.getMethod("findById", Object.class);
        Method findAllByParentId = StudentRepository.class.getMethod(
            "findAllByParentId",
            UUID.class,
            org.springframework.data.domain.Pageable.class
        );

        assertThat(entityGraph(findById)).isNotNull();
        assertThat(entityGraph(findById).attributePaths()).containsExactly("parent");
        assertThat(entityGraph(findAllByParentId)).isNotNull();
        assertThat(entityGraph(findAllByParentId).attributePaths()).containsExactly("parent");
    }

    @Nested
    @DisplayName("Validation methods")
    class ValidationMethods {

        @Test
        @DisplayName("should reject creation without responsável")
        void shouldRejectCreationWithoutResponsavel() {
            assertThatThrownBy(() -> new Student(
                "João Silva",
                "61999999999",
                "joao@email.com",
                LocalDate.of(2010, 5, 10),
                "12345678901",
                "Escola Central",
                null,
                address()
            ))
                .isInstanceOf(InvalidStudentException.class)
                .hasMessage("O estudante deve ter exatamente um responsável");
        }

        @Test
        @DisplayName("should create when values are valid")
        void shouldCreateWhenValuesAreValid() {
            Parent responsavel = parent(
                UUID.fromString("00000000-0000-0000-0000-000000000101"),
                "Maria Silva",
                "maria@email.com",
                "61977777777",
                "98765432100"
            );

            Student input = new Student(
                "João Silva",
                "61999999999",
                "joao@email.com",
                LocalDate.of(2010, 5, 10),
                "12345678901",
                "Escola Central",
                responsavel,
                address()
            );

            input.setId(UUID.fromString("00000000-0000-0000-0000-000000000201"));

            assertThat(input.getName()).isEqualTo("João Silva");
            assertThat(input.getContact()).isEqualTo("61999999999");
            assertThat(input.getEmail()).isEqualTo("joao@email.com");
            assertThat(input.getBirthdate()).isEqualTo(LocalDate.of(2010, 5, 10));
            assertThat(input.getCpf()).isEqualTo("12345678901");
            assertThat(input.getSchool()).isEqualTo("Escola Central");
            assertThat(input.getId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000201"));
            assertThat(input.getParent()).isSameAs(responsavel);
            assertThat(input.getAddress().getCity()).isEqualTo("Brasília");
        }

        @Test
        @DisplayName("should update details without changing cpf")
        void shouldUpdateDetailsWithoutChangingCpf() {
            Parent initialResponsavel = parent(
                UUID.fromString("00000000-0000-0000-0000-000000000101"),
                "Maria Silva",
                "maria@email.com",
                "61977777777",
                "98765432100"
            );

            Student input = new Student(
                "João Silva",
                "61999999999",
                "joao@email.com",
                LocalDate.of(2010, 5, 10),
                "12345678901",
                "Escola Central",
                initialResponsavel,
                address()
            );

            Parent updatedParent = parent(
                UUID.fromString("00000000-0000-0000-0000-000000000102"),
                "Carlos Silva",
                "carlos@email.com",
                "61988888888",
                "98765432100"
            );
            Address updatedAddress = new Address();
            updatedAddress.setStreet("Rua B");
            updatedAddress.setDistrict("Asa Sul");
            updatedAddress.setCity("Brasília");
            updatedAddress.setState(BrazilianStates.DF);
            updatedAddress.setZip("70200000");
            updatedAddress.setComplement("Apto 101");

            input.updateDetails(
                "João Pedro",
                "61911111111",
                "joao.pedro@email.com",
                LocalDate.of(2011, 6, 15),
                "Escola Atualizada",
                updatedParent,
                updatedAddress
            );

            assertThat(input.getName()).isEqualTo("João Pedro");
            assertThat(input.getContact()).isEqualTo("61911111111");
            assertThat(input.getEmail()).isEqualTo("joao.pedro@email.com");
            assertThat(input.getBirthdate()).isEqualTo(LocalDate.of(2011, 6, 15));
            assertThat(input.getSchool()).isEqualTo("Escola Atualizada");
            assertThat(input.getParent()).isSameAs(updatedParent);
            assertThat(input.getAddress().getStreet()).isEqualTo("Rua B");
            assertThat(input.getCpf()).isEqualTo("12345678901");
        }

        @Test
        @DisplayName("should throw when cpf is blank during creation")
        void shouldThrowWhenCpfIsBlankDuringCreation() {
            assertThatThrownBy(() -> new Student(
                "João Silva",
                "61999999999",
                "joao@email.com",
                LocalDate.of(2010, 5, 10),
                " ",
                "Escola Central",
                parent(
                    UUID.fromString("00000000-0000-0000-0000-000000000101"),
                    "Maria Silva",
                    "maria@email.com",
                    "61977777777",
                    "98765432100"
                ),
                address()
            ))
                .isInstanceOf(InvalidStudentException.class)
                .hasMessage("CPF do estudante não pode estar vazio");
        }

        @Test
        @DisplayName("should throw when parent is null during update")
        void shouldThrowWhenParentIsNullDuringUpdate() {
            Student input = student();

            assertThatThrownBy(() -> input.updateDetails(
                "João Silva",
                "61999999999",
                "joao@email.com",
                LocalDate.of(2010, 5, 10),
                "Escola Central",
                null,
                address()
            ))
                .isInstanceOf(InvalidStudentException.class)
                .hasMessage("O estudante deve ter exatamente um responsável");
        }

        @Test
        @DisplayName("should throw when address is null during update")
        void shouldThrowWhenAddressIsNullDuringUpdate() {
            Student input = new Student();

            assertThatThrownBy(() -> input.updateDetails(
                "João Silva",
                "61999999999",
                "joao@email.com",
                LocalDate.of(2010, 5, 10),
                "Escola Central",
                parent(
                    UUID.fromString("00000000-0000-0000-0000-000000000101"),
                    "Maria Silva",
                    "maria@email.com",
                    "61977777777",
                    "98765432100"
                ),
                null
            ))
                .isInstanceOf(InvalidStudentException.class)
                .hasMessage("O endereço do aluno não pode ser nulo");
        }
    }

    private Student student() {
        Student student = new Student(
            "João Silva",
            "61999999999",
            "joao@email.com",
            LocalDate.of(2010, 5, 10),
            "12345678901",
            "Escola Central",
            parent(
                UUID.fromString("00000000-0000-0000-0000-000000000101"),
                "Maria Silva",
                "maria@email.com",
                "61977777777",
                "98765432100"
            ),
            address()
        );
        student.setId(UUID.fromString("00000000-0000-0000-0000-000000000201"));
        return student;
    }

    private Parent parent(UUID id, String name, String email, String contact, String cpf) {
        Parent parent = new Parent(name, email, contact, cpf);
        parent.setId(id);
        return parent;
    }

    private EntityGraph entityGraph(Method method) {
        return method.getAnnotation(EntityGraph.class);
    }

    private Address address() {
        Address address = new Address();
        address.setStreet("Rua A");
        address.setDistrict("Centro");
        address.setCity("Brasília");
        address.setState(BrazilianStates.DF);
        address.setZip("70000000");
        address.setComplement("Casa");
        return address;
    }
}

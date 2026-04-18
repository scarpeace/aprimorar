package com.aprimorar.api.domain.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.student.exception.InvalidStudentException;
import com.aprimorar.api.enums.BrazilianStates;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Nested
    @DisplayName("Validation methods")
    class ValidationMethods {

        @Test
        @DisplayName("should create when values are valid")
        void shouldCreateWhenValuesAreValid() {
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

            assertThat(input.getName()).isEqualTo("João Silva");
            assertThat(input.getContact()).isEqualTo("61999999999");
            assertThat(input.getEmail()).isEqualTo("joao@email.com");
            assertThat(input.getBirthdate()).isEqualTo(LocalDate.of(2010, 5, 10));
            assertThat(input.getCpf()).isEqualTo("12345678901");
            assertThat(input.getSchool()).isEqualTo("Escola Central");
            assertThat(input.getParent().getName()).isEqualTo("Maria Silva");
            assertThat(input.getAddress().getCity()).isEqualTo("Brasília");
        }

        @Test
        @DisplayName("should update details without changing cpf")
        void shouldUpdateDetailsWithoutChangingCpf() {
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

            Parent updatedParent = new Parent("Carlos Silva", "carlos@email.com", "61988888888", "98765432100");
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
            assertThat(input.getParent().getName()).isEqualTo("Carlos Silva");
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
                parent(),
                address()
            ))
                .isInstanceOf(InvalidStudentException.class)
                .hasMessage("CPF do estudante não pode estar vazio");
        }

        @Test
        @DisplayName("should throw when parent is null during update")
        void shouldThrowWhenParentIsNullDuringUpdate() {
            Student input = new Student();

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
                .hasMessage("O ID do responsável não pode ser nulo");
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
                parent(),
                null
            ))
                .isInstanceOf(InvalidStudentException.class)
                .hasMessage("O endereço do aluno não pode ser nulo");
        }
    }

    private Parent parent() {
        return new Parent("Maria Silva", "maria@email.com", "61977777777", "98765432100");
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

package com.aprimorar.api.domain.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aprimorar.api.domain.employee.exception.InvalidEmployeeException;
import com.aprimorar.api.enums.Duty;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Nested
    @DisplayName("Validation methods")
    class ValidationMethods {

        @Test
        @DisplayName("should create when values are valid")
        void shouldCreateWhenValuesAreValid() {
            Employee input = new Employee(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );

            assertThat(input.getName()).isEqualTo("Ana Paula");
            assertThat(input.getBirthdate()).isEqualTo(LocalDate.of(1990, 8, 20));
            assertThat(input.getPix()).isEqualTo("ana@email.com");
            assertThat(input.getContact()).isEqualTo("61988888888");
            assertThat(input.getCpf()).isEqualTo("10987654321");
            assertThat(input.getEmail()).isEqualTo("ana@email.com");
            assertThat(input.getDuty()).isEqualTo(Duty.TEACHER);
        }

        @Test
        @DisplayName("should update details without changing cpf")
        void shouldUpdateDetailsWithoutChangingCpf() {
            Employee input = new Employee(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );

            input.updateDetails(
                "Ana Beatriz",
                LocalDate.of(1991, 1, 10),
                "ana.pix",
                "11999998888",
                "ana.beatriz@email.com",
                Duty.ADM
            );

            assertThat(input.getName()).isEqualTo("Ana Beatriz");
            assertThat(input.getBirthdate()).isEqualTo(LocalDate.of(1991, 1, 10));
            assertThat(input.getPix()).isEqualTo("ana.pix");
            assertThat(input.getContact()).isEqualTo("11999998888");
            assertThat(input.getEmail()).isEqualTo("ana.beatriz@email.com");
            assertThat(input.getDuty()).isEqualTo(Duty.ADM);
            assertThat(input.getCpf()).isEqualTo("10987654321");
        }

        @Test
        @DisplayName("should throw when name is blank during creation")
        void shouldThrowWhenNameIsBlankDuringCreation() {
            assertThatThrownBy(() -> new Employee(
                " ",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("Nome do colaborador não pode ser null");
        }

        @Test
        @DisplayName("should throw when cpf is blank during creation")
        void shouldThrowWhenCpfIsBlankDuringCreation() {
            assertThatThrownBy(() -> new Employee(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                " ",
                "ana@email.com",
                Duty.TEACHER
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("CPF do colaborador não pode ser null");
        }

        @Test
        @DisplayName("should throw when birthdate is null")
        void shouldThrowWhenBirthdateIsNull() {
            Employee input = new Employee();

            assertThatThrownBy(() -> input.updateDetails(
                "Ana Paula",
                null,
                "ana@email.com",
                "61988888888",
                "ana@email.com",
                Duty.TEACHER
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("A data de nascimento do colaborador não pode ser null");
        }

        @Test
        @DisplayName("should throw when pix is blank")
        void shouldThrowWhenPixIsBlank() {
            Employee input = new Employee();

            assertThatThrownBy(() -> input.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                " ",
                "61988888888",
                "ana@email.com",
                Duty.TEACHER
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("Pix do colaborador não pode ser null");
        }

        @Test
        @DisplayName("should throw when contact is blank")
        void shouldThrowWhenContactIsBlank() {
            Employee input = new Employee();

            assertThatThrownBy(() -> input.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                " ",
                "ana@email.com",
                Duty.TEACHER
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("Contato do colaborador não pode ser null");
        }

        @Test
        @DisplayName("should throw when email is blank")
        void shouldThrowWhenEmailIsBlank() {
            Employee input = new Employee();

            assertThatThrownBy(() -> input.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                " ",
                Duty.TEACHER
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("Email do colaborador não pode ser null");
        }

        @Test
        @DisplayName("should throw when duty is null")
        void shouldThrowWhenDutyIsNull() {
            Employee input = new Employee();

            assertThatThrownBy(() -> input.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "ana@email.com",
                null
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("Função do colaborador não pode ser null");
        }
    }
}

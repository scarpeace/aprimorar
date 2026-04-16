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
        @DisplayName("should update details when values are valid")
        void shouldUpdateDetailsWhenValuesAreValid() {
            // Arrange
            Employee input = new Employee();

            // Act
            input.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );

            // Assert
            assertThat(input.getName()).isEqualTo("Ana Paula");
            assertThat(input.getBirthdate()).isEqualTo(LocalDate.of(1990, 8, 20));
            assertThat(input.getPix()).isEqualTo("ana@email.com");
            assertThat(input.getContact()).isEqualTo("61988888888");
            assertThat(input.getCpf()).isEqualTo("10987654321");
            assertThat(input.getEmail()).isEqualTo("ana@email.com");
            assertThat(input.getDuty()).isEqualTo(Duty.TEACHER);
        }

        @Test
        @DisplayName("should throw when name is blank")
        void shouldThrowWhenNameIsBlank() {
            // Arrange
            Employee input = new Employee();

            // Act + Assert
            assertThatThrownBy(() -> input.updateDetails(
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
        @DisplayName("should throw when birthdate is null")
        void shouldThrowWhenBirthdateIsNull() {
            // Arrange
            Employee input = new Employee();

            // Act + Assert
            assertThatThrownBy(() -> input.updateDetails(
                "Ana Paula",
                null,
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("A data de nascimento do colaborador não pode ser null");
        }

        @Test
        @DisplayName("should throw when pix is blank")
        void shouldThrowWhenPixIsBlank() {
            // Arrange
            Employee input = new Employee();

            // Act + Assert
            assertThatThrownBy(() -> input.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                " ",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("Pix do colaborador não pode ser null");
        }

        @Test
        @DisplayName("should throw when contact is blank")
        void shouldThrowWhenContactIsBlank() {
            // Arrange
            Employee input = new Employee();

            // Act + Assert
            assertThatThrownBy(() -> input.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                " ",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("Contato do colaborador não pode ser null");
        }

        @Test
        @DisplayName("should throw when cpf is blank")
        void shouldThrowWhenCpfIsBlank() {
            // Arrange
            Employee input = new Employee();

            // Act + Assert
            assertThatThrownBy(() -> input.updateDetails(
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
        @DisplayName("should throw when email is blank")
        void shouldThrowWhenEmailIsBlank() {
            // Arrange
            Employee input = new Employee();

            // Act + Assert
            assertThatThrownBy(() -> input.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                " ",
                Duty.TEACHER
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("Email do colaborador não pode ser null");
        }

        @Test
        @DisplayName("should throw when duty is null")
        void shouldThrowWhenDutyIsNull() {
            // Arrange
            Employee input = new Employee();

            // Act + Assert
            assertThatThrownBy(() -> input.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                null
            ))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessage("Função do colaborador não pode ser null");
        }
    }
}

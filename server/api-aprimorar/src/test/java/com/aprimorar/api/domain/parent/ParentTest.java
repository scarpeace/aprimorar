package com.aprimorar.api.domain.parent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aprimorar.api.domain.parent.exception.InvalidParentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParentTest {

    @Nested
    @DisplayName("Validation methods")
    class ValidationMethods {

        @Test
        @DisplayName("should create when values are valid")
        void shouldCreateWhenValuesAreValid() {
            Parent input = new Parent("Maria Silva", "maria@email.com", "61977777777", "98765432100");

            assertThat(input.getName()).isEqualTo("Maria Silva");
            assertThat(input.getEmail()).isEqualTo("maria@email.com");
            assertThat(input.getContact()).isEqualTo("61977777777");
            assertThat(input.getCpf()).isEqualTo("98765432100");
        }

        @Test
        @DisplayName("should update details without changing cpf")
        void shouldUpdateDetailsWithoutChangingCpf() {
            Parent input = new Parent("Maria Silva", "maria@email.com", "61977777777", "98765432100");

            input.updateDetails("Maria Souza", "maria.souza@email.com", "11988887777");

            assertThat(input.getName()).isEqualTo("Maria Souza");
            assertThat(input.getEmail()).isEqualTo("maria.souza@email.com");
            assertThat(input.getContact()).isEqualTo("11988887777");
            assertThat(input.getCpf()).isEqualTo("98765432100");
        }

        @Test
        @DisplayName("should throw when cpf is blank during creation")
        void shouldThrowWhenCpfIsBlankDuringCreation() {
            assertThatThrownBy(() -> new Parent("Maria Silva", "maria@email.com", "61977777777", " "))
                .isInstanceOf(InvalidParentException.class)
                .hasMessage("CPF do responsável é obrigatório");
        }

        @Test
        @DisplayName("should throw when name is blank during update")
        void shouldThrowWhenNameIsBlankDuringUpdate() {
            Parent input = new Parent();

            assertThatThrownBy(() -> input.updateDetails(" ", "maria@email.com", "61977777777"))
                .isInstanceOf(InvalidParentException.class)
                .hasMessage("Nome do responsável é obrigatório");
        }

        @Test
        @DisplayName("should throw when email is blank during update")
        void shouldThrowWhenEmailIsBlankDuringUpdate() {
            Parent input = new Parent();

            assertThatThrownBy(() -> input.updateDetails("Maria Silva", " ", "61977777777"))
                .isInstanceOf(InvalidParentException.class)
                .hasMessage("Email do responsável é obrigatório");
        }

        @Test
        @DisplayName("should throw when contact is blank during update")
        void shouldThrowWhenContactIsBlankDuringUpdate() {
            Parent input = new Parent();

            assertThatThrownBy(() -> input.updateDetails("Maria Silva", "maria@email.com", " "))
                .isInstanceOf(InvalidParentException.class)
                .hasMessage("Contato do responsável é obrigatório");
        }
    }
}

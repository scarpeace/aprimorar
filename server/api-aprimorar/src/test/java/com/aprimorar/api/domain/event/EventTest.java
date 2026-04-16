package com.aprimorar.api.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.event.exception.InvalidEventException;
import com.aprimorar.api.domain.event.exception.NotAllowedToUpdateEventException;
import com.aprimorar.api.domain.student.Student;
import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.enums.EventContent;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EventTest {

    private static final Instant EVENT_START = Instant.parse("2026-03-25T13:00:00Z");
    private static final Instant EVENT_END = Instant.parse("2026-03-25T14:00:00Z");
    private static final Instant CURRENT_TIME = Instant.parse("2026-03-24T10:00:00Z");

    @Nested
    @DisplayName("Validation methods")
    class ValidationMethods {

        @Test
        @DisplayName("should update details when values are valid")
        void shouldUpdateDetailsWhenValuesAreValid() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );

            // Act
            input.updateDetails(
                "Aula de matemática",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student,
                employee
            );

            // Assert
            assertThat(input.getTitle()).isEqualTo("AULA - Col: Ana Paula - João Silva");
            assertThat(input.getDescription()).isEqualTo("Aula de matemática");
            assertThat(input.getStartDate()).isEqualTo(EVENT_START);
            assertThat(input.getEndDateTime()).isEqualTo(EVENT_END);
            assertThat(input.getPayment()).isEqualByComparingTo(BigDecimal.valueOf(80));
            assertThat(input.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(120));
            assertThat(input.getContent()).isEqualTo(EventContent.AULA);
            assertThat(input.getStudent()).isEqualTo(student);
            assertThat(input.getEmployee()).isEqualTo(employee);
        }

        @Test
        @DisplayName("should allow blank description")
        void shouldAllowBlankDescription() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );

            // Act
            input.updateDetails(
                " ",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student,
                employee
            );

            // Assert
            assertThat(input.getDescription()).isEqualTo(" ");
        }

        @Test
        @DisplayName("should throw when end date is before start date")
        void shouldThrowWhenEndDateIsBeforeStartDate() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );

            // Act + Assert
            assertThatThrownBy(() ->
                input.updateDetails(
                    "Descrição",
                    EVENT_END,
                    EVENT_START,
                    BigDecimal.valueOf(80),
                    BigDecimal.valueOf(120),
                    EventContent.AULA,
                    student,
                    employee
                )
            )
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Data de fim do evento não pode ser anterior a data de inicio");
        }

        @Test
        @DisplayName("should throw when price is less than payment")
        void shouldThrowWhenPriceIsLessThanPayment() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );

            // Act + Assert
            assertThatThrownBy(() ->
                input.updateDetails(
                    "Descrição",
                    EVENT_START,
                    EVENT_END,
                    BigDecimal.valueOf(100),
                    BigDecimal.valueOf(80),
                    EventContent.AULA,
                    student,
                    employee
                )
            )
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("O valor do evento não pode ser menor que o pagamento");
        }

        @Test
        @DisplayName("should throw when price is less than fifty")
        void shouldThrowWhenPriceIsLessThanFifty() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );

            // Act + Assert
            assertThatThrownBy(() ->
                input.updateDetails(
                    "Descrição",
                    EVENT_START,
                    EVENT_END,
                    BigDecimal.valueOf(40),
                    BigDecimal.valueOf(49),
                    EventContent.AULA,
                    student,
                    employee
                )
            )
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("O valor do evento não pode ser menor que R$50,00");
        }

        @Test
        @DisplayName("should throw when content is null")
        void shouldThrowWhenContentIsNull() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );

            // Act + Assert
            assertThatThrownBy(() ->
                input.updateDetails(
                    "Descrição",
                    EVENT_START,
                    EVENT_END,
                    BigDecimal.valueOf(80),
                    BigDecimal.valueOf(120),
                    null,
                    student,
                    employee
                )
            )
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("O conteúdo do evento é obrigatório");
        }

        @Test
        @DisplayName("should validate dates for creation when end date is in the future")
        void shouldValidateDatesForCreationWhenEndDateIsInTheFuture() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );
            input.updateDetails(
                "Descrição válida",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student,
                employee
            );

            // Act
            input.validateDatesForCreation(CURRENT_TIME);

            // Assert
            assertThat(input.getEndDateTime()).isEqualTo(EVENT_END);
        }

        @Test
        @DisplayName("should throw when end date is in the past during creation")
        void shouldThrowWhenEndDateIsInThePastDuringCreation() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );
            input.updateDetails(
                "Descrição válida",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student,
                employee
            );
            Instant now = EVENT_END.plusSeconds(1);

            // Act + Assert
            assertThatThrownBy(() -> input.validateDatesForCreation(now))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Data de fim do evento não pode estar no passado");
        }

        @Test
        @DisplayName("should validate edit window when deadline is still open")
        void shouldValidateEditWindowWhenDeadlineIsStillOpen() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );
            input.updateDetails(
                "Descrição válida",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student,
                employee
            );
            Instant now = EVENT_END.plusSeconds(20L * 24 * 60 * 60);

            // Act
            input.validateEditWindow(now);

            // Assert
            assertThat(input.getEndDateTime()).isEqualTo(EVENT_END);
        }

        @Test
        @DisplayName("should throw when edit window is closed")
        void shouldThrowWhenEditWindowIsClosed() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );
            input.updateDetails(
                "Descrição válida",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student,
                employee
            );
            Instant now = EVENT_END.plusSeconds(20L * 24 * 60 * 60 + 1);

            // Act + Assert
            assertThatThrownBy(() -> input.validateEditWindow(now))
                .isInstanceOf(NotAllowedToUpdateEventException.class)
                .hasMessage("A janela de 20 dias para editar as informações do evento encerrou");
        }

        @Test
        @DisplayName("should update details and generate title")
        void shouldUpdateDetailsAndGenerateTitle() {
            // Arrange
            Event input = new Event();
            Student student = new Student();
            student.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
            student.setName("João Silva");

            Employee employee = new Employee();
            employee.setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
            employee.updateDetails(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );

            // Act
            input.updateDetails(
                "Revisão para prova",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.MENTORIA,
                student,
                employee
            );

            // Assert
            assertThat(input.getTitle()).isEqualTo("MENTORIA - Col: Ana Paula - João Silva");
            assertThat(input.getDescription()).isEqualTo("Revisão para prova");
            assertThat(input.getStartDate()).isEqualTo(EVENT_START);
            assertThat(input.getEndDateTime()).isEqualTo(EVENT_END);
            assertThat(input.getPayment()).isEqualByComparingTo(BigDecimal.valueOf(80));
            assertThat(input.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(120));
            assertThat(input.getContent()).isEqualTo(EventContent.MENTORIA);
            assertThat(input.getStudent()).isEqualTo(student);
            assertThat(input.getEmployee()).isEqualTo(employee);
        }
    }
}

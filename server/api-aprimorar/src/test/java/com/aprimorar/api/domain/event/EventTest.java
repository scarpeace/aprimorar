package com.aprimorar.api.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.event.exception.InvalidEventException;
import com.aprimorar.api.domain.event.exception.NotAllowedToUpdateEventException;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.student.Student;
import com.aprimorar.api.enums.BrazilianStates;
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
    private static final UUID STUDENT_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UUID EMPLOYEE_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    @Nested
    @DisplayName("Validation methods")
    class ValidationMethods {

        @Test
        @DisplayName("should update details when values are valid")
        void shouldUpdateDetailsWhenValuesAreValid() {
            Event input = new Event();
            Student student = student();
            Employee employee = employee();

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
            Event input = new Event();

            input.updateDetails(
                " ",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student(),
                employee()
            );

            assertThat(input.getDescription()).isEqualTo(" ");
        }

        @Test
        @DisplayName("should throw when end date is before start date")
        void shouldThrowWhenEndDateIsBeforeStartDate() {
            Event input = new Event();

            assertThatThrownBy(() -> input.updateDetails(
                "Descrição",
                EVENT_END,
                EVENT_START,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student(),
                employee()
            ))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Data de fim do evento não pode ser anterior a data de inicio");
        }

        @Test
        @DisplayName("should throw when price is less than payment")
        void shouldThrowWhenPriceIsLessThanPayment() {
            Event input = new Event();

            assertThatThrownBy(() -> input.updateDetails(
                "Descrição",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(80),
                EventContent.AULA,
                student(),
                employee()
            ))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("O valor do evento não pode ser menor que o pagamento");
        }

        @Test
        @DisplayName("should throw when price is less than fifty")
        void shouldThrowWhenPriceIsLessThanFifty() {
            Event input = new Event();

            assertThatThrownBy(() -> input.updateDetails(
                "Descrição",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(40),
                BigDecimal.valueOf(49),
                EventContent.AULA,
                student(),
                employee()
            ))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("O valor do evento não pode ser menor que R$50,00");
        }

        @Test
        @DisplayName("should throw when content is null")
        void shouldThrowWhenContentIsNull() {
            Event input = new Event();

            assertThatThrownBy(() -> input.updateDetails(
                "Descrição",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                null,
                student(),
                employee()
            ))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("O conteúdo do evento é obrigatório");
        }

        @Test
        @DisplayName("should validate dates for creation when end date is in the future")
        void shouldValidateDatesForCreationWhenEndDateIsInTheFuture() {
            Event input = validEvent();

            input.validateDatesForCreation(CURRENT_TIME);

            assertThat(input.getEndDateTime()).isEqualTo(EVENT_END);
        }

        @Test
        @DisplayName("should throw when end date is in the past during creation")
        void shouldThrowWhenEndDateIsInThePastDuringCreation() {
            Event input = validEvent();
            Instant now = EVENT_END.plusSeconds(1);

            assertThatThrownBy(() -> input.validateDatesForCreation(now))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Data de fim do evento não pode estar no passado");
        }

        @Test
        @DisplayName("should validate edit window when deadline is still open")
        void shouldValidateEditWindowWhenDeadlineIsStillOpen() {
            Event input = validEvent();
            Instant now = EVENT_END.plusSeconds(20L * 24 * 60 * 60);

            input.validateEditWindow(now);

            assertThat(input.getEndDateTime()).isEqualTo(EVENT_END);
        }

        @Test
        @DisplayName("should throw when edit window is closed")
        void shouldThrowWhenEditWindowIsClosed() {
            Event input = validEvent();
            Instant now = EVENT_END.plusSeconds(20L * 24 * 60 * 60 + 1);

            assertThatThrownBy(() -> input.validateEditWindow(now))
                .isInstanceOf(NotAllowedToUpdateEventException.class)
                .hasMessage("A janela de 20 dias para editar as informações do evento encerrou");
        }

        @Test
        @DisplayName("should update details and generate title")
        void shouldUpdateDetailsAndGenerateTitle() {
            Event input = new Event();
            Student student = student();
            Employee employee = employee();

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

        private Event validEvent() {
            Event input = new Event();
            input.updateDetails(
                "Descrição válida",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student(),
                employee()
            );
            return input;
        }

        private Student student() {
            Parent parent = new Parent("Maria Silva", "maria@email.com", "61977777777", "98765432100");
            Address address = new Address();
            address.setStreet("Rua A");
            address.setDistrict("Centro");
            address.setCity("Brasília");
            address.setState(BrazilianStates.DF);
            address.setZip("70000000");
            address.setComplement("Casa");

            Student student = new Student(
                "João Silva",
                "61999999999",
                "joao@email.com",
                LocalDate.of(2010, 5, 10),
                "12345678901",
                "Escola Central",
                parent,
                address
            );
            student.setId(STUDENT_ID);
            return student;
        }

        private Employee employee() {
            Employee employee = new Employee(
                "Ana Paula",
                LocalDate.of(1990, 8, 20),
                "ana@email.com",
                "61988888888",
                "10987654321",
                "ana@email.com",
                Duty.TEACHER
            );
            employee.setId(EMPLOYEE_ID);
            return employee;
        }
    }
}

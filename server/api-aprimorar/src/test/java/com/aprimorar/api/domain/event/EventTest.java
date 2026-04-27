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
import org.junit.jupiter.api.Test;

class EventTest {

    private static final Instant EVENT_START = Instant.parse("2026-03-25T13:00:00Z");
    private static final Instant EVENT_END = Instant.parse("2026-03-25T14:00:00Z");
    private static final Instant CURRENT_TIME = Instant.parse("2026-03-24T10:00:00Z");
    private static final Instant AFTER_EVENT_END = Instant.parse("2026-03-26T10:00:00Z");
    private static final UUID STUDENT_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UUID EMPLOYEE_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    @Test
    @DisplayName("should initialize financial booleans as false")
    void shouldInitializeFinancialBooleansAsFalse() {
        Event event = createEvent(EVENT_START, EVENT_END, CURRENT_TIME);

        assertThat(event.isStudentCharged()).isFalse();
        assertThat(event.isEmployeePaid()).isFalse();
    }

    @Test
    @DisplayName("should set student charged explicitly")
    void shouldSetStudentChargedExplicitly() {
        Event event = createEvent(EVENT_START, EVENT_END, CURRENT_TIME);

        event.setStudentCharged(true);

        assertThat(event.isStudentCharged()).isTrue();
    }

    @Test
    @DisplayName("should set employee paid explicitly")
    void shouldSetEmployeePaidExplicitly() {
        Event event = createEvent(EVENT_START, EVENT_END, CURRENT_TIME);

        event.setEmployeePaid(true);

        assertThat(event.isEmployeePaid()).isTrue();
    }

    @Test
    @DisplayName("should update details when values are valid")
    void shouldUpdateDetailsWhenValuesAreValid() {
        Event event = createEvent(EVENT_START, EVENT_END, CURRENT_TIME);
        Student student = student();
        Employee employee = employee();

        event.update(
            "Aula de matemática",
            EVENT_START,
            EVENT_END,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(120),
            EventContent.AULA,
            student,
            employee,
            CURRENT_TIME
        );

        assertThat(event.getTitle()).isEqualTo("AULA - Col: Ana Paula - João Silva");
        assertThat(event.getDescription()).isEqualTo("Aula de matemática");
        assertThat(event.getStartDate()).isEqualTo(EVENT_START);
        assertThat(event.getEndDateTime()).isEqualTo(EVENT_END);
        assertThat(event.getPayment()).isEqualByComparingTo(BigDecimal.valueOf(80));
        assertThat(event.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(120));
        assertThat(event.getContent()).isEqualTo(EventContent.AULA);
        assertThat(event.getStudent()).isEqualTo(student);
        assertThat(event.getEmployee()).isEqualTo(employee);
    }

    @Test
    @DisplayName("should allow blank description")
    void shouldAllowBlankDescription() {
        Event event = createEvent(EVENT_START, EVENT_END, CURRENT_TIME);

        event.update(
            " ",
            EVENT_START,
            EVENT_END,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(120),
            EventContent.AULA,
            student(),
            employee(),
            CURRENT_TIME
        );

        assertThat(event.getDescription()).isEqualTo(" ");
    }

    @Test
    @DisplayName("should throw when end date is before start date")
    void shouldThrowWhenEndDateIsBeforeStartDate() {
        assertThatThrownBy(() ->
            new Event(
                "Descrição",
                EVENT_END,
                EVENT_START,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student(),
                employee(),
                CURRENT_TIME
            )
        )
            .isInstanceOf(InvalidEventException.class)
            .hasMessage("Data de fim do evento não pode ser anterior a data de inicio");
    }

    @Test
    @DisplayName("should throw when price is less than payment")
    void shouldThrowWhenPriceIsLessThanPayment() {
        assertThatThrownBy(() ->
            new Event(
                "Descrição",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(80),
                EventContent.AULA,
                student(),
                employee(),
                CURRENT_TIME
            )
        )
            .isInstanceOf(InvalidEventException.class)
            .hasMessage("O valor do evento não pode ser menor que o pagamento");
    }

    @Test
    @DisplayName("should throw when price is less than fifty")
    void shouldThrowWhenPriceIsLessThanFifty() {
        assertThatThrownBy(() ->
            new Event(
                "Descrição",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(40),
                BigDecimal.valueOf(49),
                EventContent.AULA,
                student(),
                employee(),
                CURRENT_TIME
            )
        )
            .isInstanceOf(InvalidEventException.class)
            .hasMessage("O valor do evento não pode ser menor que R$50,00");
    }

    @Test
    @DisplayName("should throw when content is null")
    void shouldThrowWhenContentIsNull() {
        assertThatThrownBy(() ->
            new Event(
                "Descrição",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                null,
                student(),
                employee(),
                CURRENT_TIME
            )
        )
            .isInstanceOf(InvalidEventException.class)
            .hasMessage("O conteúdo do evento é obrigatório");
    }

    @Test
    @DisplayName("should throw when end date is in the past during creation")
    void shouldThrowWhenEndDateIsInThePastDuringCreation() {
        assertThatThrownBy(() ->
            new Event(
                "Descrição válida",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student(),
                employee(),
                AFTER_EVENT_END
            )
        )
            .isInstanceOf(InvalidEventException.class)
            .hasMessage("Data de fim do evento não pode estar no passado");
    }

    @Test
    @DisplayName("should allow updating finished event inside edit window")
    void shouldAllowUpdatingFinishedEventInsideEditWindow() {
        Event event = createEvent(EVENT_START, EVENT_END, CURRENT_TIME);
        Instant editTime = EVENT_END.plusSeconds(5 * 24 * 60 * 60);

        event.update(
            "Revisão para prova",
            EVENT_START,
            EVENT_END,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(120),
            EventContent.MENTORIA,
            student(),
            employee(),
            editTime
        );

        assertThat(event.getDescription()).isEqualTo("Revisão para prova");
        assertThat(event.getContent()).isEqualTo(EventContent.MENTORIA);
    }

    @Test
    @DisplayName("should throw when edit window is closed")
    void shouldThrowWhenEditWindowIsClosed() {
        Event event = createEvent(EVENT_START, EVENT_END, CURRENT_TIME);
        Instant now = EVENT_END.plusSeconds(20L * 24 * 60 * 60 + 1);

        assertThatThrownBy(() ->
            event.update(
                "Descrição válida",
                EVENT_START,
                EVENT_END,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student(),
                employee(),
                now
            )
        )
            .isInstanceOf(NotAllowedToUpdateEventException.class)
            .hasMessage("A janela de 20 dias para editar as informações do evento encerrou");
    }

    private Event createEvent(Instant startDate, Instant endDate, Instant now) {
        return new Event(
            "Descrição válida",
            startDate,
            endDate,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(120),
            EventContent.AULA,
            student(),
            employee(),
            now
        );
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

package aprimorar.event.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import aprimorar.address.api.Address;
import aprimorar.employee.internal.Employee;
import aprimorar.event.api.exception.InvalidEventException;
import aprimorar.event.api.exception.NotAllowedToUpdateEventException;
import aprimorar.parent.internal.Parent;
import aprimorar.student.internal.Student;
import aprimorar.shared.enums.BrazilianStates;
import aprimorar.registration.shared.enums.Duty;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventTest {

    private static final Instant EVENT_START = Instant.parse("2026-03-25T13:00:00Z");
    private static final Double EVENT_DURATION = 1.0;
    private static final Instant EVENT_END = Instant.parse("2026-03-25T14:00:00Z");
    private static final Instant CURRENT_TIME = Instant.parse("2026-03-24T10:00:00Z");
    private static final Instant AFTER_EVENT_END = Instant.parse("2026-03-26T10:00:00Z");
    private static final UUID STUDENT_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UUID EMPLOYEE_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    @Test
    @DisplayName("should initialize financial booleans as false")
    void shouldInitializeFinancialBooleansAsFalse() {
        Event event = createEvent(EVENT_START, EVENT_DURATION, CURRENT_TIME);

        assertThat(event.getStudentChargeDate()).isNull();
        assertThat(event.isEmployeePaid()).isFalse();
    }

    @Test
    @DisplayName("should toggle student charged and set date")
    void shouldToggleStudentChargedAndSetDate() {
        Event event = createEvent(EVENT_START, EVENT_DURATION, CURRENT_TIME);
        Instant now = Instant.now();

        event.toggleStudentCharge(now);
        assertThat(event.isStudentCharged()).isTrue();
        assertThat(event.getStudentChargeDate()).isEqualTo(now);

        event.toggleStudentCharge(now);
        assertThat(event.isStudentCharged()).isFalse();
        assertThat(event.getStudentChargeDate()).isNull();
    }

    @Test
    @DisplayName("should toggle employee paid and set date")
    void shouldToggleEmployeePaidAndSetDate() {
        Event event = createEvent(EVENT_START, EVENT_DURATION, CURRENT_TIME);
        Instant now = Instant.now();

        event.toggleEmployeePayment(now);
        assertThat(event.isEmployeePaid()).isTrue();
        assertThat(event.getEmployeePaymentDate()).isEqualTo(now);

        event.toggleEmployeePayment(now);
        assertThat(event.isEmployeePaid()).isFalse();
        assertThat(event.getEmployeePaymentDate()).isNull();
    }

    @Test
    @DisplayName("should update details when values are valid")
    void shouldUpdateDetailsWhenValuesAreValid() {
        Event event = createEvent(EVENT_START, EVENT_DURATION, CURRENT_TIME);
        Student student = student();
        Employee employee = employee();

        event.update(
            "Aula de matemática",
            EVENT_START,
            EVENT_DURATION,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(120),
            EventContent.AULA,
            student.getId(),
            student.getName(),
            employee.getId(),
            employee.getName(),
            CURRENT_TIME
        );

        assertThat(event.getTitle()).isEqualTo("AULA - Col: Ana Paula - João Silva");
        assertThat(event.getDescription()).isEqualTo("Aula de matemática");
        assertThat(event.getStartDate()).isEqualTo(EVENT_START);
        assertThat(event.getEndDateTime()).isEqualTo(EVENT_END);
        assertThat(event.getPayment()).isEqualByComparingTo(BigDecimal.valueOf(80));
        assertThat(event.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(120));
        assertThat(event.getContent()).isEqualTo(EventContent.AULA);
        assertThat(event.getStudentId()).isEqualTo(student.getId());
    }

    @Test
    @DisplayName("should allow blank description")
    void shouldAllowBlankDescription() {
        Event event = createEvent(EVENT_START, EVENT_DURATION, CURRENT_TIME);
        Student student = student();
        Employee employee = employee();

        event.update(
            " ",
            EVENT_START,
            EVENT_DURATION,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(120),
            EventContent.AULA,
            student.getId(),
            student.getName(),
            employee.getId(),
            employee.getName(),
            CURRENT_TIME
        );

        assertThat(event.getDescription()).isEqualTo(" ");
    }

    @Test
    @DisplayName("should throw when end date is before start date")
    void shouldThrowWhenEndDateIsBeforeStartDate() {
        Student student = student();
        Employee employee = employee();
        assertThatThrownBy(() ->
            new Event(
                "Descrição",
                EVENT_START,
                -1.0,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student.getId(),
                student.getName(),
                employee.getId(),
                employee.getName(),
                CURRENT_TIME
            )
        )
            .isInstanceOf(InvalidEventException.class)
            .hasMessage("Data de fim do evento não pode ser anterior a data de inicio");
    }

    @Test
    @DisplayName("should throw when price is less than payment")
    void shouldThrowWhenPriceIsLessThanPayment() {
        Student student = student();
        Employee employee = employee();
        assertThatThrownBy(() ->
            new Event(
                "Descrição",
                EVENT_START,
                EVENT_DURATION,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(80),
                EventContent.AULA,
                student.getId(),
                student.getName(),
                employee.getId(),
                employee.getName(),
                CURRENT_TIME
            )
        )
            .isInstanceOf(InvalidEventException.class)
            .hasMessage("O valor do evento não pode ser menor que o pagamento");
    }

    @Test
    @DisplayName("should throw when price is less than fifty")
    void shouldThrowWhenPriceIsLessThanFifty() {
        Student student = student();
        Employee employee = employee();
        assertThatThrownBy(() ->
            new Event(
                "Descrição",
                EVENT_START,
                EVENT_DURATION,
                BigDecimal.valueOf(40),
                BigDecimal.valueOf(49),
                EventContent.AULA,
                student.getId(),
                student.getName(),
                employee.getId(),
                employee.getName(),
                CURRENT_TIME
            )
        )
            .isInstanceOf(InvalidEventException.class)
            .hasMessage("O valor do evento não pode ser menor que R$50,00");
    }

    @Test
    @DisplayName("should throw when content is null")
    void shouldThrowWhenContentIsNull() {
        Student student = student();
        Employee employee = employee();
        assertThatThrownBy(() ->
            new Event(
                "Descrição",
                EVENT_START,
                EVENT_DURATION,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                null,
                student.getId(),
                student.getName(),
                employee.getId(),
                employee.getName(),
                CURRENT_TIME
            )
        )
            .isInstanceOf(InvalidEventException.class)
            .hasMessage("O conteúdo do evento é obrigatório");
    }

    @Test
    @DisplayName("should throw when end date is in the past during creation")
    void shouldThrowWhenEndDateIsInThePastDuringCreation() {
        Student student = student();
        Employee employee = employee();
        assertThatThrownBy(() ->
            new Event(
                "Descrição válida",
                EVENT_START,
                EVENT_DURATION,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student.getId(),
                student.getName(),
                employee.getId(),
                employee.getName(),
                AFTER_EVENT_END
            )
        )
            .isInstanceOf(InvalidEventException.class)
            .hasMessage("Data de fim do evento não pode estar no passado");
    }

    @Test
    @DisplayName("should allow updating finished event inside edit window")
    void shouldAllowUpdatingFinishedEventInsideEditWindow() {
        Event event = createEvent(EVENT_START, EVENT_DURATION, CURRENT_TIME);
        Instant editTime = EVENT_END.plusSeconds(5 * 24 * 60 * 60);
        Student student = student();
        Employee employee = employee();

        event.update(
            "Revisão para prova",
            EVENT_START,
            EVENT_DURATION,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(120),
            EventContent.MENTORIA,
            student.getId(),
            student.getName(),
            employee.getId(),
            employee.getName(),
            editTime
        );

        assertThat(event.getDescription()).isEqualTo("Revisão para prova");
        assertThat(event.getContent()).isEqualTo(EventContent.MENTORIA);
    }

    @Test
    @DisplayName("should throw when edit window is closed")
    void shouldThrowWhenEditWindowIsClosed() {
        Event event = createEvent(EVENT_START, EVENT_DURATION, CURRENT_TIME);
        Instant now = EVENT_END.plusSeconds(20L * 24 * 60 * 60 + 1);
        Student student = student();
        Employee employee = employee();

        assertThatThrownBy(() ->
            event.update(
                "Descrição válida",
                EVENT_START,
                EVENT_DURATION,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(120),
                EventContent.AULA,
                student.getId(),
                student.getName(),
                employee.getId(),
                employee.getName(),
                now
            )
        )
            .isInstanceOf(NotAllowedToUpdateEventException.class)
            .hasMessage("A janela de 20 dias para editar as informações do evento encerrou");
    }

    private Event createEvent(Instant startDate, Double duration, Instant now) {
        Student student = student();
        Employee employee = employee();
        return new Event(
            "Descrição válida",
            startDate,
            duration,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(120),
            EventContent.AULA,
            student.getId(),
            student.getName(),
            employee.getId(),
            employee.getName(),
            now
        );
    }

    private Student student() {
        UUID parentId = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");
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
            parentId,
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

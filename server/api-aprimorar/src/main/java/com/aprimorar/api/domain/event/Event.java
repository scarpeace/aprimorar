package com.aprimorar.api.domain.event;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.event.exception.InvalidEventException;
import com.aprimorar.api.domain.event.exception.NotAllowedToUpdateEventException;
import com.aprimorar.api.domain.student.Student;
import com.aprimorar.api.enums.EventContent;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

// TODO: Adicionar campos do google calendar para a implementação
@Entity
@Table(name = "tb_events")
public class Event extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date_time", nullable = false)
    private Instant startDate;

    @Column(name = "end_date_time", nullable = false)
    private Instant endDate;

    @Column(name = "payment", precision = 19, scale = 2, nullable = false)
    private BigDecimal payment;

    @Column(name = "price", precision = 19, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventContent content;

    @Column(name = "student_charged", nullable = false)
    private boolean studentCharged;

    @Column(name = "employee_paid", nullable = false)
    private boolean employeePaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    protected Event() {}

    public Event(
        String description,
        Instant startDate,
        Instant endDate,
        BigDecimal payment,
        BigDecimal price,
        EventContent content,
        Student student,
        Employee employee,
        Instant now
    ) {
        validateDates(startDate, endDate, now);
        validateAmounts(payment, price);
        validateParticipants(student, employee);
        validateContent(content);

        this.title = buildTitle(content, student, employee);
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payment = payment;
        this.price = price;
        this.content = content;
        this.student = student;
        this.employee = employee;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDateTime() {
        return endDate;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public EventContent getContent() {
        return content;
    }

    public boolean isStudentCharged() {
        return studentCharged;
    }

    public boolean isEmployeePaid() {
        return employeePaid;
    }

    public Student getStudent() {
        return student;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void update(
        String description,
        Instant startDate,
        Instant endDate,
        BigDecimal payment,
        BigDecimal price,
        EventContent content,
        Student student,
        Employee employee,
        Instant now
    ) {
        validateEditWindow(now);
        validateDates(startDate, endDate);
        validateAmounts(payment, price);
        validateParticipants(student, employee);
        validateContent(content);

        this.title = buildTitle(content, student, employee);
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payment = payment;
        this.price = price;
        this.content = content;
        this.student = student;
        this.employee = employee;
    }

    public void setStudentCharged(boolean studentCharged) {
        this.studentCharged = studentCharged;
    }

    public void setEmployeePaid(boolean employeePaid) {
        this.employeePaid = employeePaid;
    }

    public void validateEditWindow(Instant now) {
        if (this.endDate != null && now.isAfter(this.endDate.plus(20, ChronoUnit.DAYS))) {
            throw new NotAllowedToUpdateEventException(
                "A janela de 20 dias para editar as informações do evento encerrou"
            );
        }
    }

    private void validateDates(Instant startDate, Instant endDate, Instant now) {
        validateDates(startDate, endDate);
        if (endDate.isBefore(now)) {
            throw new InvalidEventException("Data de fim do evento não pode estar no passado");
        }
    }

    private void validateDates(Instant startDate, Instant endDate) {
        if (startDate == null) {
            throw new InvalidEventException("Data de início do evento é obrigatório");
        }
        if (endDate == null) {
            throw new InvalidEventException("Data de término do evento é obrigatório");
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidEventException("Data de fim do evento não pode ser anterior a data de inicio");
        }
    }

    private void validateAmounts(BigDecimal payment, BigDecimal price) {
        if (payment == null) {
            throw new InvalidEventException("Pagamento do evento é obrigatório");
        }
        if (price == null) {
            throw new InvalidEventException("Valor do evento é obrigatório");
        }
        if (price.compareTo(payment) < 0) {
            throw new InvalidEventException("O valor do evento não pode ser menor que o pagamento");
        }
        if (price.compareTo(BigDecimal.valueOf(50)) < 0) {
            throw new InvalidEventException("O valor do evento não pode ser menor que R$50,00");
        }
    }

    private void validateParticipants(Student student, Employee employee) {
        if (student == null) {
            throw new InvalidEventException("Um evento não pode existir sem um estudante");
        }
        if (employee == null) {
            throw new InvalidEventException("Um evento não pode existir sem um colaborador");
        }
    }

    private void validateContent(EventContent content) {
        if (content == null) {
            throw new InvalidEventException("O conteúdo do evento é obrigatório");
        }
    }

    private String buildTitle(EventContent content, Student student, Employee employee) {
        return content + " - Col: " + employee.getName() + " - " + student.getName();
    }
}

package com.aprimorar.api.domain.event.internal;

import com.aprimorar.api.domain.event.api.exception.InvalidEventException;
import com.aprimorar.api.domain.event.api.exception.NotAllowedToUpdateEventException;
import com.aprimorar.api.enums.EventContent;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

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

    @Column(name = "employee_payment_date", nullable = true)
    private Instant employeePaymentDate;

    @Column(name = "student_charge_date", nullable = true)
    private Instant studentChargeDate;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    protected Event() {}

    public Event(
        String description,
        Instant startDate,
        Double duration,
        BigDecimal payment,
        BigDecimal price,
        EventContent content,
        UUID studentId,
        String studentName,
        UUID employeeId,
        String employeeName,
        Instant now
    ) {
        this.endDate = calculateEndDate(startDate, duration);
        validateDates(startDate);
        validateNotPast(now);
        validateAmounts(payment, price);
        validateParticipants(studentId, employeeId);
        validateContent(content);

        this.title = buildTitle(content, studentName, employeeName);
        this.description = description;
        this.startDate = startDate;
        this.payment = payment;
        this.price = price;
        this.content = content;
        this.studentId = studentId;
        this.employeeId = employeeId;
    }

    @Transient
    public Double getDuration() {
        return (double) Duration.between(startDate, endDate).toMinutes() / 60.0;
    }

    @Transient
    public BigDecimal getProfit() {
        return price.subtract(payment);
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

    public Instant getEmployeePaymentDate() {
        return employeePaymentDate;
    }

    public Instant getStudentChargeDate() {
        return studentChargeDate;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public Event update(
        String description,
        Instant startDate,
        Double duration,
        BigDecimal payment,
        BigDecimal price,
        EventContent content,
        UUID studentId,
        String studentName,
        UUID employeeId,
        String employeeName,
        Instant now
    ) {
        this.endDate = calculateEndDate(startDate, duration);
        validateEditWindow(now);
        validateDates(startDate);
        validateAmounts(payment, price);
        validateParticipants(studentId, employeeId);
        validateContent(content);

        this.title = buildTitle(content, studentName, employeeName);
        this.description = description;
        this.startDate = startDate;
        this.payment = payment;
        this.price = price;
        this.content = content;
        this.studentId = studentId;
        this.employeeId = employeeId;

        return this;
    }

    public void toggleStudentCharge(Instant now) {
        if (this.studentChargeDate != null) {
            this.studentChargeDate = null;
        } else {
            this.studentChargeDate = now;
        }
    }

    public void toggleEmployeePayment(Instant now) {
        if (this.employeePaymentDate != null) {
            this.employeePaymentDate = null;
        } else {
            this.employeePaymentDate = now;
        }
    }

    @Transient
    public boolean isStudentCharged() {
        return this.studentChargeDate != null;
    }

    @Transient
    public boolean isEmployeePaid() {
        return this.employeePaymentDate != null;
    }

    public static Instant calculateEndDate(Instant startDate, Double duration) {
        if (startDate == null) {
            throw new InvalidEventException("Data de início do evento é obrigatório");
        }
        if (duration == null) {
            throw new InvalidEventException("Duração do evento é obrigatória");
        }
        return startDate.plus((long) (duration * 60), ChronoUnit.MINUTES);
    }

    public void validateEditWindow(Instant now) {
        if (this.endDate != null && now.isAfter(this.endDate.plus(20, ChronoUnit.DAYS))) {
            throw new NotAllowedToUpdateEventException("A janela de 20 dias para editar as informações do evento encerrou");
        }
    }

    private void validateDates(Instant startDate) {
        if (this.endDate.isBefore(startDate)) {
            throw new InvalidEventException("Data de fim do evento não pode ser anterior a data de inicio");
        }
    }

    private void validateNotPast(Instant now) {
        if (this.endDate.isBefore(now)) {
            throw new InvalidEventException("Data de fim do evento não pode estar no passado");
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

    private void validateParticipants(UUID studentId, UUID employeeId) {
        if (studentId == null) {
            throw new InvalidEventException("Um evento não pode existir sem um estudante");
        }
        if (employeeId == null) {
            throw new InvalidEventException("Um evento não pode existir sem um colaborador");
        }
    }

    private void validateContent(EventContent content) {
        if (content == null) {
            throw new InvalidEventException("O conteúdo do evento é obrigatório");
        }
    }

    private String buildTitle(EventContent content, String studentName, String employeeName) {
        return content + " - Col: " + employeeName + " - " + studentName;
    }
}

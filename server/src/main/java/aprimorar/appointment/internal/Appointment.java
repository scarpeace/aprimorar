package aprimorar.appointment.internal;

import aprimorar.appointment.api.AppointmentContent;
import aprimorar.appointment.api.exception.InvalidAppointmentException;
import aprimorar.appointment.api.exception.NotAllowedToUpdateAppointmentException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.io.Serializable;
import java.util.UUID;

// TODO: Adicionar campos do google calendar para a implementação
@Entity
@Getter
@Table(name = "tb_appointments")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @Column(name = "payment", precision = 19, scale = 2, nullable = false)
    private BigDecimal payment;

    @Column(name = "price", precision = 19, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentContent content;

    @Column(name = "employee_payment_date", nullable = true)
    private Instant employeePaymentDate;

    @Column(name = "student_charge_date", nullable = true)
    private Instant studentChargeDate;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    protected Appointment() {}

    public Appointment(
        String description,
        Instant startDate,
        Double duration,
        BigDecimal payment,
        BigDecimal price,
        AppointmentContent content,
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
        this.studentName = studentName;
        this.employeeName = employeeName;
    }

    @Transient
    public Double getDuration() {
        return Duration.between(startDate, endDate).toMinutes() / 60.0;
    }

    @Transient
    public BigDecimal getProfit() {
        return price.subtract(payment);
    }

    public Appointment update(
        String description,
        Instant startDate,
        Double duration,
        BigDecimal payment,
        BigDecimal price,
        AppointmentContent content,
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
        this.studentName = studentName;
        this.employeeId = employeeId;
        this.employeeName = employeeName;

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

    public static Instant calculateEndDate(Instant startDate, Double duration) {
        if (startDate == null) {
            throw new InvalidAppointmentException("Data de início do evento é obrigatório");
        }
        if (duration == null) {
            throw new InvalidAppointmentException("Duração do evento é obrigatória");
        }
        return startDate.plus((long) (duration * 60), ChronoUnit.MINUTES);
    }

    public void validateEditWindow(Instant now) {
        if (this.endDate != null && now.isAfter(this.endDate.plus(20, ChronoUnit.DAYS))) {
            throw new NotAllowedToUpdateAppointmentException("A janela de 20 dias para editar as informações do evento encerrou");
        }
    }

    private void validateDates(Instant startDate) {
        if (this.endDate.isBefore(startDate)) {
            throw new InvalidAppointmentException("Data de fim do evento não pode ser anterior a data de inicio");
        }
    }

    private void validateNotPast(Instant now) {
        if (this.endDate.isBefore(now)) {
            throw new InvalidAppointmentException("Data de fim do evento não pode estar no passado");
        }
    }

    private void validateAmounts(BigDecimal payment, BigDecimal price) {
        if (payment == null) {
            throw new InvalidAppointmentException("Pagamento do evento é obrigatório");
        }
        if (price == null) {
            throw new InvalidAppointmentException("Valor do evento é obrigatório");
        }
        if (price.compareTo(payment) < 0) {
            throw new InvalidAppointmentException("O valor do evento não pode ser menor que o pagamento");
        }
        if (price.compareTo(BigDecimal.valueOf(50)) < 0) {
            throw new InvalidAppointmentException("O valor do evento não pode ser menor que R$50,00");
        }
    }

    private void validateParticipants(UUID studentId, UUID employeeId) {
        if (studentId == null) {
            throw new InvalidAppointmentException("Um evento não pode existir sem um estudante");
        }
        if (employeeId == null) {
            throw new InvalidAppointmentException("Um evento não pode existir sem um colaborador");
        }
    }

    private void validateContent(AppointmentContent content) {
        if (content == null) {
            throw new InvalidAppointmentException("O conteúdo do evento é obrigatório");
        }
    }

    private String buildTitle(AppointmentContent content, String studentName, String employeeName) {
        return content + " - Col: " + employeeName + " - " + studentName;
    }
}

package com.aprimorar.api.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import com.aprimorar.api.domain.event.command.EventCommand;
import com.aprimorar.api.domain.event.exception.InvalidEventException;
import com.aprimorar.api.domain.event.exception.NotAllowedToUpdateEventException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.aprimorar.api.domain.employee.EmployeeEntity;
import com.aprimorar.api.domain.student.StudentEntity;
import com.aprimorar.api.enums.EventContent;

// TODO: Add googleCalendarEventId when calendar integration is enabled.

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "price", precision = 19, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "payment", precision = 19, scale = 2, nullable = false)
    private BigDecimal payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private StudentEntity studentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", nullable = false)
    private EmployeeEntity employeeEntity;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;

    public void create(
            EventCommand command,
            StudentEntity student,
            EmployeeEntity employee,
            LocalDateTime now
    ) {
        validateCommand(command, student, employee, now);
        applyCommand(command, student, employee);
    }

    public void update(
            EventCommand command,
            StudentEntity student,
            EmployeeEntity employee,
            LocalDateTime now
    ) {
        validateCommand(command, student, employee, now);
        validateEditWindow(command);
        applyCommand(command, student, employee);
    }

    private void validateCommand(EventCommand command, StudentEntity student, EmployeeEntity employee, LocalDateTime now) {
        validateRequiredFields(command, student, employee);
        validatePriceAndPayment(command.price(), command.payment());
        validateParticipants(student, employee);
        validateDates(command.startDateTime(), command.endDateTime(), now);
    }

    private void validateRequiredFields(EventCommand command, StudentEntity student, EmployeeEntity employee) {
        if (command.title() == null || command.title().isBlank()) {
            throw new InvalidEventException("Título do evento é obrigatório");
        }
        if (command.startDateTime() == null) {
            throw new InvalidEventException("Data de início do evento é obrigatório");
        }
        if (command.endDateTime() == null) {
            throw new InvalidEventException("Data de término do evento é obrigatório");
        }
        if (command.price() == null) {
            throw new InvalidEventException("Valor do evento é obrigatório");
        }
        if (command.payment() == null) {
            throw new InvalidEventException("Pagamento do evento é obrigatório");
        }
        if (command.content() == null) {
            throw new InvalidEventException("O conteúdo do evento é obrigatório");
        }
        if (student == null) {
            throw new InvalidEventException("Um evento não pode existir sem um estudante");
        }
        if (employee == null) {
            throw new InvalidEventException("Um evento não pode existir sem um colaborador");
        }
    }

    private void validatePriceAndPayment(BigDecimal price, BigDecimal payment) {
        if (price.compareTo(payment) < 0) {
            throw new InvalidEventException("O valor do evento não pode ser menor que o pagamento");
        }
        if (price.compareTo(BigDecimal.valueOf(50)) < 0) {
            throw new InvalidEventException("O valor do evento não pode ser menor que R$50,00");
        }
    }

    private void assignParticipants(StudentEntity student, EmployeeEntity employee) {
        this.studentEntity = student;
        this.employeeEntity = employee;
    }

    private void validateParticipants(StudentEntity student, EmployeeEntity employee) {
        if (student.isArchived()) {
            throw new InvalidEventException("Evento não pode ter estudantes arquivados");
        }
        if (employee.isArchived()) {
            throw new InvalidEventException("Evento não pode ter colaboradores arquivados");
        }
    }

    private void validateDates(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime now) {
        if (endDateTime.isBefore(now)) {
            throw new InvalidEventException("Data de fim do evento nao pode estar no passado");
        }
        if (endDateTime.isBefore(startDateTime)) {
            throw new InvalidEventException("Data de fim do evento nao pode ser anterior a data de inicio");
        }
    }

    private void validateEditWindow(EventCommand command){
        if(command.endDateTime().isAfter(endDateTime.plusWeeks(2))){
            throw new NotAllowedToUpdateEventException("A janela para a editar as informações do evento encerrou");
        }
    }

    private void applyCommand(EventCommand command, StudentEntity student, EmployeeEntity employee) {
        this.title = command.title();
        this.description = command.description();
        this.startDateTime = command.startDateTime();
        this.endDateTime = command.endDateTime();
        this.price = command.price();
        this.payment = command.payment();
        this.content = command.content();
        assignParticipants(student, employee);
    }





}

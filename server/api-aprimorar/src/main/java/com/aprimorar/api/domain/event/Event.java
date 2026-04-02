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
import java.time.LocalDateTime;
import java.util.UUID;

// TODO: Adicionar campos do google calendar para a implementação
@Entity
@Table(name = "tb_events")
public class Event extends BaseEntity {

    //TODO: Eu acho que esse titulo nao é necessario sem o google calendar
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "price", precision = 19, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "payment", precision = 19, scale = 2, nullable = false)
    private BigDecimal payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    public Event() {}

    @Override
    public UUID getId() {
        return super.getId();
    }

    @Override
    public void setId(UUID id) {
        super.setId(id);
    }

    @Override
    public Instant getCreatedAt() {
        return super.getCreatedAt();
    }

    @Override
    public Instant getUpdatedAt() {
        return super.getUpdatedAt();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidEventException("Título do evento é obrigatório");
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        if (startDate == null) {
            throw new InvalidEventException("Data de início do evento é obrigatório");
        }
        this.startDate = startDate;
    }

    public LocalDateTime getEndDateTime() {
        return endDate;
    }

    public void setEndDateTime(LocalDateTime endDate) {
        if (endDate == null) {
            throw new InvalidEventException("Data de término do evento é obrigatório");
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidEventException("Data de fim do evento não pode ser anterior a data de inicio");
        }

        this.endDate = endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidEventException("Valor do evento é obrigatório");
        }
        if (price.compareTo(this.payment) < 0) {
            throw new InvalidEventException("O valor do evento não pode ser menor que o pagamento");
        }
        if (price.compareTo(BigDecimal.valueOf(50)) < 0) {
            throw new InvalidEventException("O valor do evento não pode ser menor que R$50,00");
        }
        this.price = price;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        if (payment == null) {
            throw new InvalidEventException("Pagamento do evento é obrigatório");
        }
        this.payment = payment;
    }

    public EventContent getContent() {
        return content;
    }

    public void setContent(EventContent content) {
        if (content == null) {
            throw new InvalidEventException("O conteúdo do evento é obrigatório");
        }
        this.content = content;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        if (student == null) {
            throw new InvalidEventException("Um evento não pode existir sem um estudante");
        }
        this.student = student;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if (employee == null) {
            throw new InvalidEventException("Um evento não pode existir sem um colaborador");
        }
        this.employee = employee;
    }

    public void validateForCreation() {
        validateCommonRules();
        if (this.endDate != null && this.endDate.isBefore(LocalDateTime.now())) {
            throw new InvalidEventException("Data de fim do evento não pode estar no passado");
        }
    }

    public void validateForUpdate() {
        validateCommonRules();
    }

    private void validateCommonRules() {
        setTitle(this.title);
        setDescription(this.description);
        setStartDate(this.startDate);
        setEndDateTime(this.endDate);
        setPrice(this.price);
        setPayment(this.payment);
        setContent(this.content);
        setStudent(this.student);
        setEmployee(this.employee);
    }

    public void validateEditWindow() {
        if (this.endDate != null && LocalDateTime.now().isAfter(this.endDate.plusDays(20))) {
            throw new NotAllowedToUpdateEventException(
                "A janela de 20 dias para editar as informações do evento encerrou"
            );
        }
    }
}

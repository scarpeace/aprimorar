package com.aprimorar.api.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import com.aprimorar.api.domain.event.exception.EventWithArchivedEmployeeException;
import com.aprimorar.api.domain.event.exception.EventWithArchivedStudentException;
import com.aprimorar.api.domain.event.exception.InvalidEventException;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.aprimorar.api.domain.employee.EmployeeEntity;
import com.aprimorar.api.domain.student.StudentEntity;
import com.aprimorar.api.enums.EventContent;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO: Add googleCalendarEventId when calendar integration is enabled.

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "price", precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "payment", precision = 19, scale = 2)
    private BigDecimal payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventContent content;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private StudentEntity studentEntity;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private EmployeeEntity employeeEntity;

    //TODO Investigar porque esses aqui não estão funcionando
    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    public void assignParticipants(StudentEntity student, EmployeeEntity employee) {
        if(student == null || employee == null){
            throw new InvalidEventException("Ambos os participantes são obrigatórios no evento");
        }

        if(student.getArchivedAt() != null){
            throw new EventWithArchivedStudentException("Evento não pode ter estudantes arquivados");
        }

        if(employee.getArchivedAt() != null){
            throw new EventWithArchivedEmployeeException("Evento não pode ter colaboradores arquivados");
        }

        this.studentEntity = student;
        this.employeeEntity = employee;
    }

    public void validateDates(LocalDateTime now) {
        if (endDateTime == null) {
            throw new InvalidEventException("Data de fim do evento e obrigatória");
        }
        if (startDateTime == null) {
            throw new InvalidEventException("Data de início do evento e obrigatória");
        }
        if (endDateTime.isBefore(now)) {
            throw new InvalidEventException("Data de fim do evento nao pode estar no passado");
        }
        if (endDateTime.isBefore(startDateTime)) {
            throw new InvalidEventException("Data de fim do evento nao pode ser anterior a data de inicio");
        }
    }


}

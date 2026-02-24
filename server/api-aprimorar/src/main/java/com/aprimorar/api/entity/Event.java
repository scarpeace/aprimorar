package com.aprimorar.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//TODO Adicionar o title, description e o googleCalendarEventId aqui

@Entity
@Table(name = "tb_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private BigDecimal price;

    private BigDecimal payment;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;
}

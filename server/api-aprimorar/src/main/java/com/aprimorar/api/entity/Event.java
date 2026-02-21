package com.aprimorar.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @NotNull(message = "Event date can't be null")
    @FutureOrPresent(message = "Event must be in the Future")
    private LocalDate date;

    @NotNull(message = "Price can't be null")
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotNull(message = "Payment can't be null")
    @DecimalMin(value = "0.0")
    private BigDecimal payment;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @NotNull(message = "Student can't be null")
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @NotNull(message = "Employee can't be null")
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    //The validation runs before the @NotNull annotations
    @AssertTrue(message = "Payment can't exceed price")
    private boolean isPaymentValid(){
        if (payment == null || price == null) {
            return true; // Skip validation if fields are null
        }
        return payment.compareTo(price) <= 0;
    }
}

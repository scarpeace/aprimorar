package com.aprimorar.api.domain.employee;


import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "tb_employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "pix", nullable = false)
    private String pix;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "duty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Duty duty;

}

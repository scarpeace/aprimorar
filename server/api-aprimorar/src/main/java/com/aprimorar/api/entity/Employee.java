package com.aprimorar.api.entity;


import com.aprimorar.api.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_employee", uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_name", columnNames = {"name"}),
        @UniqueConstraint(name = "uk_employee_contact", columnNames = {"contact"}),
        @UniqueConstraint(name = "uk_employee_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_employee_cpf", columnNames = {"cpf"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private UUID id;

    private String name;

    private String birthdate;

    private String pix;

    private String contact;

    private String cpf;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}

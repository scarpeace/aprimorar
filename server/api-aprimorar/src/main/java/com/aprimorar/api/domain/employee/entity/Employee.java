package com.aprimorar.api.domain.employee.entity;


import com.aprimorar.api.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_employee", uniqueConstraints = {
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

    @Column(nullable = false)
    private String name;

    private LocalDate birthdate;

    private String pix;

    private String contact;

    private String cpf;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @Column(name = "last_reactivated_at")
    private Instant lastReactivatedAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;


}

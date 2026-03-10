package com.aprimorar.api.domain.employee;


import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.aprimorar.api.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//TODO Eu preciso adicionar um novo campo no employee que é o employeeRole, vou deixar o role para autenticação e acesso

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

    //TODO isso aqui vai poder tirar lá na frente
    @Column(name = "last_reactivated_at")
    private Instant lastReactivatedAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;


}

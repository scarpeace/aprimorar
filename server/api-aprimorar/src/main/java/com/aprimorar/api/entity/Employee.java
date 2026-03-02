package com.aprimorar.api.entity;


import com.aprimorar.api.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_employee", uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_name", columnNames = {"name"}),
        @UniqueConstraint(name = "uk_employee_contact", columnNames = {"contact"}),
        @UniqueConstraint(name = "uk_employee_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_employee_cpf", columnNames = {"cpf"})
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private UUID id;

    private String name;

    private LocalDate birthdate;

    private String pix;

    private String contact;

    private String cpf;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean active = true;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public Employee() {
    }

    public Employee(UUID id, String name, LocalDate birthdate, String pix, String contact, String cpf, String email, Role role,
                    Boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.pix = pix;
        this.contact = contact;
        this.cpf = cpf;
        this.email = email;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPix() {
        return pix;
    }

    public void setPix(String pix) {
        this.pix = pix;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}

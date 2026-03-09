package com.aprimorar.api.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_student", uniqueConstraints = {
        @UniqueConstraint(name = "uk_student_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_student_cpf", columnNames = {"cpf"})
})
public class Student {

    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String email;

    // TODO: Define minimum and maximum student age in business rules.
    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String school;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @Column(name = "last_reactivated_at")
    private Instant lastReactivatedAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id", nullable = false)
    private Parent parent;

    @Embedded
    private Address address;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    public Student() {
    }

    public Student(UUID id, String name, String contact, String email, LocalDate birthdate, String cpf, String school,
                   Instant archivedAt, Instant lastReactivatedAt, Parent parent, Address address, Instant createdAt,
                   Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.birthdate = birthdate;
        this.cpf = cpf;
        this.school = school;
        this.archivedAt = archivedAt;
        this.lastReactivatedAt = lastReactivatedAt;
        this.parent = parent;
        this.address = address;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Instant getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(Instant archivedAt) {
        this.archivedAt = archivedAt;
    }

    public Instant getLastReactivatedAt() {
        return lastReactivatedAt;
    }

    public void setLastReactivatedAt(Instant lastReactivatedAt) {
        this.lastReactivatedAt = lastReactivatedAt;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

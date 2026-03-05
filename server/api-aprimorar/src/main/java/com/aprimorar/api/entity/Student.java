package com.aprimorar.api.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_student", uniqueConstraints = {
        @UniqueConstraint(name = "uk_student_name", columnNames = {"name"}),
        @UniqueConstraint(name = "uk_student_contact", columnNames = {"contact"}),
        @UniqueConstraint(name = "uk_student_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_student_cpf", columnNames = {"cpf"})
})
public class Student {

    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String contact;

    private String email;

    // TODO: Define minimum and maximum student age in business rules.
    private LocalDate birthdate;

    private String cpf;

    private String school;

    private Boolean active = true;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id")
    private Parent parent;

    @Embedded
    private Address address;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public Student() {
    }

    public Student(UUID id, String name, String contact, String email, LocalDate birthdate, String cpf, String school,
                   Boolean active, Parent parent, Address address, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.birthdate = birthdate;
        this.cpf = cpf;
        this.school = school;
        this.active = active;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

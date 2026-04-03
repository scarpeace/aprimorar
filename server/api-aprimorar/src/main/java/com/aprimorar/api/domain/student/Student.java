package com.aprimorar.api.domain.student;

import java.time.LocalDate;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.student.exception.InvalidStudentException;
import com.aprimorar.api.shared.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_students")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Student extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @EqualsAndHashCode.Include
    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "school", nullable = false)
    private String school;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = false)
    private Parent parent;

    @Embedded
    private Address address;

    public Student() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidStudentException("Nome do estudante não pode estar vazio");
        }
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        if (contact == null || contact.isBlank()) {
            throw new InvalidStudentException("Contato do estudante não pode estar vazio");
        }
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidStudentException("Email do estudante não pode estar vazio");
        }
        this.email = email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            throw new InvalidStudentException("A data de nascimento não pode estar vazio");
        }
        this.birthdate = birthdate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidStudentException("CPF do estudante não pode estar vazio");
        }
        this.cpf = cpf;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        if (school == null || school.isBlank()) {
            throw new InvalidStudentException("Escola do estudante não pode estar vazio");
        }
        this.school = school;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        if (parent == null) {
            throw new InvalidStudentException("O ID do responsável não pode ser nulo");
        }
        this.parent = parent;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new InvalidStudentException("O endereço do aluno não pode ser nulo");
        }
        this.address = address;
    }
}

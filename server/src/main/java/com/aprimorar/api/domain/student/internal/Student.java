package com.aprimorar.api.domain.student.internal;

import com.aprimorar.api.domain.address.api.Address;
import com.aprimorar.api.domain.student.api.exception.InvalidStudentException;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_students")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Student extends BaseEntity {

    private static final String REQUIRED_PARENT_MESSAGE = "O estudante deve ter exatamente um responsável";

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

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @Embedded
    private Address address;

    protected Student() {}

    public Student(
        String name,
        String contact,
        String email,
        LocalDate birthdate,
        String cpf,
        String school,
        UUID parentId,
        Address address
    ) {
        validateRequiredFields(name, contact, email, birthdate, school, parentId, address);
        validateCpf(cpf);

        this.name = name;
        this.contact = contact;
        this.email = email;
        this.birthdate = birthdate;
        this.cpf = cpf;
        this.school = school;
        this.parentId = parentId;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    private void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    private void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getCpf() {
        return cpf;
    }

    private void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSchool() {
        return school;
    }

    private void setSchool(String school) {
        this.school = school;
    }

    public UUID getParentId() {
        return parentId;
    }

    private void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public Address getAddress() {
        return address;
    }

    private void setAddress(Address address) {
        this.address = address;
    }

    public void updateDetails(
        String name,
        String contact,
        String email,
        LocalDate birthdate,
        String school,
        UUID parentId,
        Address address
    ) {
        validateRequiredFields(name, contact, email, birthdate, school, parentId, address);

        setName(name);
        setContact(contact);
        setEmail(email);
        setBirthdate(birthdate);
        setSchool(school);
        setParentId(parentId);
        setAddress(address);
    }

    private void validateParent(UUID parentId) {
        if (parentId == null) {
            throw new InvalidStudentException(REQUIRED_PARENT_MESSAGE);
        }
    }

    private void validateRequiredFields(
        String name,
        String contact,
        String email,
        LocalDate birthdate,
        String school,
        UUID parentId,
        Address address
    ) {
        if (name == null || name.isBlank()) {
            throw new InvalidStudentException("Nome do estudante não pode estar vazio");
        }
        if (contact == null || contact.isBlank()) {
            throw new InvalidStudentException("Contato do estudante não pode estar vazio");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidStudentException("Email do estudante não pode estar vazio");
        }
        if (birthdate == null) {
            throw new InvalidStudentException("A data de nascimento não pode estar vazio");
        }
        if (school == null || school.isBlank()) {
            throw new InvalidStudentException("Escola do estudante não pode estar vazio");
        }
        validateParent(parentId);
        if (address == null) {
            throw new InvalidStudentException("O endereço do aluno não pode ser nulo");
        }
    }

    private void validateCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidStudentException("CPF do estudante não pode estar vazio");
        }
    }
}

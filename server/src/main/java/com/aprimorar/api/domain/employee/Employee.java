package com.aprimorar.api.domain.employee;

import com.aprimorar.api.domain.employee.exception.InvalidEmployeeException;
import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_employees")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Employee extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "pix", nullable = false)
    private String pix;

    @Column(name = "contact", nullable = false)
    private String contact;

    @EqualsAndHashCode.Include
    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "duty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Duty duty;

    protected Employee() {}

    public Employee(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String cpf,
        String email,
        Duty duty
    ) {
        validateRequiredFields(name, birthdate, pix, contact, email, duty);
        validateCpf(cpf);

        this.name = name;
        this.birthdate = birthdate;
        this.pix = pix;
        this.contact = contact;
        this.cpf = cpf;
        this.email = email;
        this.duty = duty;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    private void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPix() {
        return pix;
    }

    private void setPix(String pix) {
        this.pix = pix;
    }

    public String getContact() {
        return contact;
    }

    private void setContact(String contact) {
        this.contact = contact;
    }

    public String getCpf() {
        return cpf;
    }

    private void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public Duty getDuty() {
        return duty;
    }

    private void setDuty(Duty duty) {
        this.duty = duty;
    }

    public void updateDetails(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String email,
        Duty duty
    ) {
        validateRequiredFields(name, birthdate, pix, contact, email, duty);

        setName(name);
        setBirthdate(birthdate);
        setPix(pix);
        setContact(contact);
        setEmail(email);
        setDuty(duty);
    }

    private void validateRequiredFields(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String email,
        Duty duty
    ) {
        if (name == null || name.isBlank()) {
            throw new InvalidEmployeeException("Nome do colaborador não pode ser null");
        }
        if (birthdate == null) {
            throw new InvalidEmployeeException("A data de nascimento do colaborador não pode ser null");
        }
        if (pix == null || pix.isBlank()) {
            throw new InvalidEmployeeException("Pix do colaborador não pode ser null");
        }
        if (contact == null || contact.isBlank()) {
            throw new InvalidEmployeeException("Contato do colaborador não pode ser null");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidEmployeeException("Email do colaborador não pode ser null");
        }
        if (duty == null) {
            throw new InvalidEmployeeException("Função do colaborador não pode ser null");
        }
    }

    private void validateCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidEmployeeException("CPF do colaborador não pode ser null");
        }
    }
}

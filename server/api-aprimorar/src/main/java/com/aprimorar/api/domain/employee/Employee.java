package com.aprimorar.api.domain.employee;

import com.aprimorar.api.domain.employee.exception.InvalidEmployeeException;
import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
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

    public Employee() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidEmployeeException("Nome do colaborador não pode ser null");
        }
        this.name = name;
    }

    public void setBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            throw new InvalidEmployeeException("A data de nascimento do colaborador não pode ser null");
        }
        this.birthdate = birthdate;
    }

    public void setPix(String pix) {
        if (pix == null || pix.isBlank()) {
            throw new InvalidEmployeeException("Pix do colaborador não pode ser null");
        }
        this.pix = pix;
    }

    public void setContact(String contact) {
        if (contact == null || contact.isBlank()) {
            throw new InvalidEmployeeException("Contato do colaborador não pode ser null");
        }
        this.contact = contact;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidEmployeeException("CPF do colaborador não pode ser null");
        }
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmployeeException("Email do colaborador não pode ser null");
        }
        this.email = email;
    }

    public void setDuty(Duty duty) {
        if (duty == null) {
            throw new InvalidEmployeeException("Função do colaborador não pode ser null");
        }
        this.duty = duty;
    }
}

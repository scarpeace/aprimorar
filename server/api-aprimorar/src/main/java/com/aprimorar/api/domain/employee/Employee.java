package com.aprimorar.api.domain.employee;
import java.time.LocalDate;

import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_employees")
public class Employee extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "pix", nullable = false)
    private String pix;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "duty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Duty duty;

    public Employee() {
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

    public Duty getDuty() {
        return duty;
    }

    public void setDuty(Duty duty) {
        this.duty = duty;
    }
}

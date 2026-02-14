package com.aprimorar.api.entity;


import com.aprimorar.api.enums.Role;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthdate")
    private String birthdate;

    @Column(name = "pix")
    private String pix;

    @Column(name = "contact")
    private String contact;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "role")
    private Role role;

    public Employee() {
    }

    public Employee(UUID id, String name, String birthdate, String pix, String contact, String cpf, Role role) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.pix = pix;
        this.contact = contact;
        this.cpf = cpf;
        this.role = role;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

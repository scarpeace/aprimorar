package com.aprimorar.api.domain.student;

import java.time.LocalDate;

import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.*;

import com.aprimorar.api.domain.address.Address;

@Entity
@Table(name = "tb_students")
public class Student extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "school", nullable = false)
    private String school;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = false)
    private Parent parent;

    @Embedded()
    private Address address;

    public Student() {
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

}

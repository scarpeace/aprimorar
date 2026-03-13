package com.aprimorar.api.domain.parent;


import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_parent")
public class Parent extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "contact",nullable = false)
    private String contact;

    @Column(name = "cpf",nullable = false, unique = true)
    private String cpf;

    public Parent() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}

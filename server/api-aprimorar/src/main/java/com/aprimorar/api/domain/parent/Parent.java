package com.aprimorar.api.domain.parent;

import com.aprimorar.api.domain.parent.exception.InvalidParentException;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_parent")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Parent extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contact", nullable = false)
    private String contact;

    @EqualsAndHashCode.Include
    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    protected Parent() {}

    public Parent(String name, String email, String contact, String cpf) {
        validateRequiredFields(name, email, contact);
        validateCpf(cpf);

        this.name = name;
        this.email = email;
        this.contact = contact;
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
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

    public void updateDetails(String name, String email, String contact) {
        validateRequiredFields(name, email, contact);

        setName(name);
        setEmail(email);
        setContact(contact);
    }

    private void validateRequiredFields(String name, String email, String contact) {
        if (name == null || name.isBlank()) {
            throw new InvalidParentException("Nome do responsável é obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidParentException("Email do responsável é obrigatório");
        }
        if (contact == null || contact.isBlank()) {
            throw new InvalidParentException("Contato do responsável é obrigatório");
        }
    }

    private void validateCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidParentException("CPF do responsável é obrigatório");
        }
    }
}

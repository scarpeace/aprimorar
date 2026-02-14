package com.aprimorar.api.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_parent")
public class Parent {

    @Id
    @Column(name = "parent_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "contact", unique = true, nullable = false)
    private String contact;

    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant creationTimestamp;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public Parent() {
    }

    public Parent(UUID id, String name, String email, String contact, String cpf, Instant creationTimestamp, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.cpf = cpf;
        this.creationTimestamp = creationTimestamp;
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

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

package com.aprimorar.api.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tb_parent")
public class Parent {

    @Id
    @Column(name = "parent_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "name")
    private String name;

    @Column(name = "birthdate")
    private Date birthdate;

    @Column(name = "phone")
    private String phone;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "email")
    private String email;
}

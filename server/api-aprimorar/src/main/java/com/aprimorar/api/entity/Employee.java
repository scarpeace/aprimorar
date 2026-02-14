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
}

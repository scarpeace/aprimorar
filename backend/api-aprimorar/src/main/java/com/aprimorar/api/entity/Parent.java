package com.aprimorar.api.entity;

import jakarta.persistence.*;

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
}

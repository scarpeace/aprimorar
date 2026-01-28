package com.aprimorar.api.entity;

import com.aprimorar.api.enums.Activity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_student")
public class Student {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthdate")
    private Date birthdate;

    @OneToOne(
            cascade = CascadeType.ALL,
            mappedBy = "student",
            orphanRemoval = true)
    private Address address;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "school")
    private String school;

    @OneToOne(
            cascade = CascadeType.ALL,
            mappedBy = "student",
            orphanRemoval = true)
    private Parent parent;

    @Enumerated(EnumType.STRING)
    private Activity activity;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "active")
    private Boolean active;

}

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

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "birthdate", nullable = false)
    private Date birthdate;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "school", nullable = false)
    private String school;

    @Column(name="activity", nullable = false)
    @Enumerated(EnumType.STRING)
    private Activity activity;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id")
    private Parent parent;

    @Embedded
    private Address address;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "active")
    private Boolean active;

}

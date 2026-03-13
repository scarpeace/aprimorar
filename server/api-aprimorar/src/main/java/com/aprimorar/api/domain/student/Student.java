package com.aprimorar.api.domain.student;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.aprimorar.api.domain.address.Address;

@Entity
@Getter
@Setter
@NoArgsConstructor()
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

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = false)
    private Parent parent;

    @Embedded()
    private Address address;


}

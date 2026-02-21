package com.aprimorar.api.entity;

import com.aprimorar.api.enums.Activity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_student", uniqueConstraints = {
        @UniqueConstraint(name = "uk_student_name", columnNames = {"name"}),
        @UniqueConstraint(name = "uk_student_contact", columnNames = {"contact"}),
        @UniqueConstraint(name = "uk_student_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_student_cpf", columnNames = {"cpf"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Student name can't be null")
    private String name;

    @NotNull(message = "Student contact can't be null")
    private String contact;

    @NotNull(message = "Student email can't be null")
    @Email(message = "Invalid email format")
    private String email;

    //TODO Qual a idade máxima e mínima dos alunos?
    @NotNull(message = "Student birthdate can't be null")
    @Past(message = "Birhtdate must be in the past")
    private LocalDate birthdate;

    @NotNull(message = "Student cpf can't be null")
    private String cpf;

    @NotNull(message = "Student school can't be null")
    private String school;

    @NotNull(message = "Student activity can't be null")
    @Enumerated(EnumType.STRING)
    private Activity activity;

    private Boolean active = true;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @NotNull(message = "Student parent can't be null")
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id")
    private Parent parent;

    @Embedded
    @NotNull(message = "Student address can't be null")
    private Address address;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}

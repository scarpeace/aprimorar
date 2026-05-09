package com.aprimorar.api.domain.registration.student.internal;

import com.aprimorar.api.domain.registration.address.api.Address;
import com.aprimorar.api.domain.registration.parent.internal.Parent;
import com.aprimorar.api.domain.registration.student.api.dto.StudentResponseDTO;
import com.aprimorar.api.domain.registration.student.api.exception.InvalidStudentException;
import com.aprimorar.api.shared.BaseEntity;
import com.aprimorar.api.shared.MapperUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Table(name = "tb_students")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Student extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @EqualsAndHashCode.Include
    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "school", nullable = false)
    private String school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @Embedded
    private Address address;

    protected Student() {
    }

    public Student(
            String name,
            String contact,
            String email,
            LocalDate birthdate,
            String cpf,
            String school,
            Parent parent,
            Address address
    ) {
        validateRequiredFields(name, contact, email, birthdate, school, parent, address);
        validateCpf(cpf);

        this.name = name;
        this.contact = MapperUtils.normalizeContact(contact);
        this.email = MapperUtils.normalizeEmail(email);
        this.birthdate = birthdate;
        this.cpf = MapperUtils.normalizeCpf(cpf);
        this.school = school;
        this.parent = parent;
        this.address = address;
    }

    public void updateDetails(
        String name,
        String contact,
        String email,
        LocalDate birthdate,
        String school,
        Parent parent,
        Address address
    ) {
        validateRequiredFields(name, contact, email, birthdate, school, parent, address);

        this.name = name;
        this.contact = MapperUtils.normalizeContact(contact);
        this.email = MapperUtils.normalizeEmail(email);
        this.birthdate = birthdate;
        this.school = school;
        this.parent = parent;
        this.address = address;
    }

    public StudentResponseDTO toResponseDto(Clock clock) {
        return new StudentResponseDTO(
                getId(),
                getName(),
                getContact(),
                getEmail(),
                getCpf(),
                getBirthdate(),
                getSchool(),
                calculateAge(clock),
                parent.getId(),
                getArchivedAt(),
                getUpdatedAt(),
                getCreatedAt(),
                address.toResponseDto()
        );
    }

    //TODO: Talvez esse método não precise receber um clock
    public Integer calculateAge(Clock clock) {
        LocalDate today = LocalDate.now(clock);
        return Period.between(birthdate, today).getYears();
    }

    private void validateRequiredFields(
            String name,
            String contact,
            String email,
            LocalDate birthdate,
            String school,
            Parent parent,
            Address address
    ) {
        if (parent == null) {
            throw new IllegalArgumentException("Aluno não pode ser criado sem um responsável");
        }
        if (address == null) {
            throw new IllegalArgumentException("Aluno não pode ser criado sem um endereço");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidStudentException("Nome do estudante não pode estar vazio");
        }
        if (contact == null || contact.isBlank()) {
            throw new InvalidStudentException("Contato do estudante não pode estar vazio");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidStudentException("Email do estudante não pode estar vazio");
        }
        if (birthdate == null) {
            throw new InvalidStudentException("A data de nascimento não pode estar vazio");
        }
        if (school == null || school.isBlank()) {
            throw new InvalidStudentException("Escola do estudante não pode estar vazio");
        }
    }

    private void validateCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidStudentException("CPF do estudante não pode estar vazio");
        }
    }
}

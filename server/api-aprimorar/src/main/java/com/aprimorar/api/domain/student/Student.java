package com.aprimorar.api.domain.student;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.command.ParentCommand;
import com.aprimorar.api.domain.student.command.StudentCommand;
import com.aprimorar.api.domain.student.exception.InvalidStudentException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.aprimorar.api.domain.address.Address;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tb_student", uniqueConstraints = {
        @UniqueConstraint(name = "uk_student_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_student_cpf", columnNames = {"cpf"})
})

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String school;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @Column(name = "last_reactivated_at")
    private Instant lastReactivatedAt;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id", nullable = false)
    private Parent parent;

    @Embedded
    private Address address;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    public void create(StudentCommand command) {
        validateCommand(command);
        assignParent(command.parent());
        apply(command);
    }

    public void update(StudentCommand command, ParentCommand parent, Address address) {
        validateCommand(command);
        assignParent(parent);
        apply(command);
    }

    public void archive(Instant now) {
        if (this.archivedAt == null) {
            this.archivedAt = now;
        }
    }

    public void unarchive(Instant now) {
        if (this.archivedAt != null) {
            this.archivedAt = null;
            this.lastReactivatedAt = now;
        }
    }

    public boolean isArchived() {
        return archivedAt != null;
    }

    public void assignParent(Parent parent) {
        this.parent = parent;
    }

    private void validateCommand(StudentCommand command) {
        validateRequiredFields(command, parent, address);
    }

    private void validateRequiredFields(StudentCommand command, Parent parent, Address address) {
        if (command.name() == null || command.name().isBlank()) {
            throw new InvalidStudentException("Nome do estudante não pode estar vazio");
        }
        if (command.birthdate() == null) {
            throw new InvalidStudentException("A data de nascimento não pode estar vazio");
        }
        if (command.cpf() == null || command.cpf().isBlank()) {
            throw new InvalidStudentException("CPF do estudante não pode estar vazio");
        }
        if (command.school() == null || command.school().isBlank()) {
            throw new InvalidStudentException("Escola do estudante não pode estar vazio");
        }
        if (command.contact() == null || command.contact().isBlank()) {
            throw new InvalidStudentException("Contato do estudante não pode estar vazio");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new InvalidStudentException("Email do estudante não pode estar vazio");
        }
        if (command.address() == null) {
            throw new InvalidStudentException("Endereço do estudante é obrigatório");
        }
        if (parent == null) {
            throw new InvalidStudentException("O ID do responsável não pode ser nulo");
        }
        if (address == null) {
            throw new InvalidStudentException("O endereço do aluno não pode ser nulo");
        }
    }

    private void apply(StudentCommand command) {
        this.name = command.name();
        this.birthdate = command.birthdate();
        this.cpf = command.cpf();
        this.school = command.school();
        this.contact = command.contact();
        this.email = command.email();
        this.address = command.address();
    }

}

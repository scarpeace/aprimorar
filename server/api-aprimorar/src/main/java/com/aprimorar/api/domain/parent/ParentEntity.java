package com.aprimorar.api.domain.parent;

import java.time.Instant;
import java.util.UUID;

import com.aprimorar.api.domain.parent.command.ParentCommand;
import com.aprimorar.api.domain.parent.exception.InvalidParentException;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_parent", uniqueConstraints = {
        @UniqueConstraint(name = "uk_parent_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_parent_cpf", columnNames = {"cpf"})
})
public class ParentEntity {

    @Id
    @Column(name = "parent_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String cpf;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    public void create(ParentCommand command) {
        validateRequiredFields(command);
        applyCommand(command);
    }

    public void updateDetails(ParentCommand command) {
        validateRequiredFields(command);
        applyCommand(command);
    }

    private void validateRequiredFields(ParentCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new InvalidParentException("Nome do responsável é obrigatório");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new InvalidParentException("Email do responsável é obrigatório");
        }
        if (command.contact() == null || command.contact().isBlank()) {
            throw new InvalidParentException("Contato do responsável é obrigatório");
        }
        if (command.cpf() == null || command.cpf().isBlank()) {
            throw new InvalidParentException("CPF do responsável é obrigatório");
        }
    }

    private void applyCommand(ParentCommand command) {
        this.name = command.name();
        this.email = command.email();
        this.contact = command.contact();
        this.cpf = command.cpf();
    }

}

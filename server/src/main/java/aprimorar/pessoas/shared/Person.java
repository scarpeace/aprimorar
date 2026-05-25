package aprimorar.pessoas.shared;

import aprimorar.shared.MapperUtils;
import aprimorar.shared.enums.Role;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "birthdate", nullable = true)
    private LocalDate birthdate;

    @Column(name = "cpf", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String cpf;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "pix", nullable = true)
    private String pix;

    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    protected Person() {}

    public Person(String name, LocalDate birthdate, String pix, String contact, String cpf, String email) {
        this.name = validateName(name);
        this.birthdate = validateBirthdate(birthdate);
        this.pix = pix;
        this.contact = validateContact(contact);
        this.cpf = validateCpf(cpf);
        this.email = validateEmail(email);
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void update(String name, LocalDate birthdate, String pix, String contact, String email) {
        this.name = validateName(name);
        this.birthdate = validateBirthdate(birthdate);
        this.pix = pix;
        this.contact = validateContact(contact);
        this.email = validateEmail(email);
    }

    public void archive() {
        this.active = false;
    }

    public void unarchive() {
        if (Role.ADMIN.equals(this.role)) {
            throw new IllegalStateException("O Administrador não pode ser desarquivado.");
        }
        this.active = true;
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        return name;
    }

    private LocalDate validateBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            throw new IllegalArgumentException("A data de nascimento é obrigatória");
        }
        return birthdate;
    }

    private String validateContact(String contact) {
        var normalized = MapperUtils.normalizeContact(contact);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("Contato é obrigatório");
        }
        return normalized;
    }

    private String validateCpf(String cpf) {
        var normalized = MapperUtils.normalizeCpf(cpf);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        return normalized;
    }

    private String validateEmail(String email) {
        var normalized = MapperUtils.normalizeEmail(email);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        return normalized;
    }
}

package aprimorar.shared;

import aprimorar.registration.employee.api.exception.InvalidEmployeeException;
import aprimorar.registration.shared.enums.Duty;
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

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    protected Person() {}

    protected Person(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String cpf,
        String email
    ) {
        validateRequiredFields(name, birthdate, pix, contact, email);
        validateCpf(cpf);
        this.name = name;
        this.birthdate = birthdate;
        this.contact = MapperUtils.normalizeContact(contact);
        this.pix = pix;
        this.cpf = MapperUtils.normalizeCpf(cpf);
        this.email = MapperUtils.normalizeEmail(email);
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void update(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String email
    ) {
        validateRequiredFields(name, birthdate, pix, contact, email);

        this.name = name;
        this.birthdate = birthdate;
        this.pix = pix;
        this.contact = contact;
        this.email = email;
    }

    public void archive() {
        this.active = false;
    }

    public void unarchive() {
        this.active = true;
    }

    private void validateRequiredFields(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String email
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (birthdate == null) {
            throw new IllegalArgumentException(
                "A data de nascimento é obrigatório"
            );
        }
        if (pix == null || pix.isBlank()) {
            throw new IllegalArgumentException("Pix é obrigatório");
        }
        if (contact == null || contact.isBlank()) {
            throw new IllegalArgumentException("Contato é obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
    }

    private void validateCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
    }
}

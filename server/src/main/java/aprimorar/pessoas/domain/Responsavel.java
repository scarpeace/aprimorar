package aprimorar.pessoas.domain;

import aprimorar.shared.MapperUtils;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Table(name = "responsaveis")
public class Responsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 50)
    private String name;

    @Column(name = "data_nascimento")
    private LocalDate birthdate;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cpf", nullable = false, unique = true))
    private Cpf cpf;

    @Column(name = "telefone", nullable = false)
    private String contact;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private Email email;

    @Column(name = "user_id", unique = true)
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    protected Responsavel() {}

    public Responsavel(
        String name,
        LocalDate birthdate,
        String contact,
        String cpf,
        String email
    ) {
        this.name = validateName(name);
        this.birthdate = birthdate;
        this.contact = validateContact(contact);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
    }

    public void updateDetails(
        String name,
        LocalDate birthdate,
        String contact,
        String email
    ) {
        this.name = validateName(name);
        this.birthdate = birthdate;
        this.contact = validateContact(contact);
        this.email = new Email(email);
    }

    public String getCpf() {
        return cpf.value();
    }

    public String getEmail() {
        return email.value();
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("STATE: Nome do resposável é obrigatório");
        }
        return name;
    }

    private String validateContact(String contact) {
        var normalized = MapperUtils.normalizeContact(contact);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("STATE: Contato do responsável é obrigatório");
        }
        return normalized;
    }

}

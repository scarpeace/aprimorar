package aprimorar.pessoas.domain;

import aprimorar.pessoas.shared.Cpf;
import aprimorar.pessoas.shared.Email;
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
@Table(name = "alunos")
@Getter
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 50)
    private String name;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate birthdate;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cpf", nullable = false, unique = true))
    private Cpf cpf;

    @Column(name = "telefone", nullable = false)
    private String contact;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private Email email;

    @Column(name = "escola")
    private String school;

    @Column(name = "responsavel_id", nullable = false)
    private UUID parentId;

    @Column(name = "ativo", nullable = false)
    private Boolean active = true;

    @Column(name = "user_id", unique = true)
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Embedded
    private Endereco endereco;

    protected Aluno() {
    }

    public Aluno(
            String name,
            LocalDate birthdate,
            String pix,
            String contact,
            String cpf,
            String email,
            String school,
            UUID parentId,
            Endereco endereco
    ) {
        validateRequiredFields(endereco, parentId);
        this.name = validateName(name);
        this.birthdate = validateBirthdate(birthdate);
        this.contact = validateContact(contact);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
        this.school = school;
        this.parentId = parentId;
        this.endereco = endereco;
    }

    public void update(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String email,
        String school,
        UUID parentId,
        Endereco endereco
    ) {
        validateRequiredFields(endereco, parentId);
        this.name = validateName(name);
        this.birthdate = validateBirthdate(birthdate);
        this.contact = validateContact(contact);
        this.email = new Email(email);
        this.school = school;
        this.parentId = parentId;
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf.value();
    }

    public String getEmail() {
        return email.value();
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void archive() {
        this.active = false;
    }

    public void unarchive() {
        this.active = true;
    }

    private void validateRequiredFields(Endereco endereco, UUID parentId) {
        if (endereco == null) {
            throw new IllegalArgumentException("STATE: Endereço do aluno é obrigatório");
        }
        if (parentId == null) {
            throw new IllegalArgumentException("STATE: Aluno não pode ser cadastrado sem um responsável");
        }
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("STATE: Nome do aluno é obrigatório");
        }
        return name;
    }

    private LocalDate validateBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            throw new IllegalArgumentException("STATE: Data de nascimento do aluno é obrigatória");
        }
        return birthdate;
    }

    private String validateContact(String contact) {
        var normalized = MapperUtils.normalizeContact(contact);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("STATE: Contato do aluno é obrigatório");
        }
        return normalized;
    }
}

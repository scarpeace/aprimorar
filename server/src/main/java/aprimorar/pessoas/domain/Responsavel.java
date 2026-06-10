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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "responsaveis")
public class Responsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cpf", nullable = false, unique = true))
    private Cpf cpf;

    @Column(name = "telefone", nullable = false)
    private String telefone;

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
        this.nome = validateName(name);
        this.dataNascimento = birthdate;
        this.telefone = validateContact(contact);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
    }

    public void updateDetails(
        String name,
        LocalDate birthdate,
        String contact,
        String email
    ) {
        this.nome = validateName(name);
        this.dataNascimento = birthdate;
        this.telefone = validateContact(contact);
        this.email = new Email(email);
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

    public String getCpf() {
        return cpf.value();
    }

    public String getEmail() {
        return email.value();
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String name) {
		this.nome = name;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate birthdate) {
		this.dataNascimento = birthdate;
	}

	public void setCpf(Cpf cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String contact) {
		this.telefone = contact;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

}

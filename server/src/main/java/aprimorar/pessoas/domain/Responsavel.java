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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    protected Responsavel() {}

    public Responsavel(
        String nome,
        LocalDate dataNascimento,
        String telefone,
        String cpf,
        String email
    ) {
        this.nome = validateName(nome);
        this.dataNascimento = dataNascimento;
        this.telefone = validateContact(telefone);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
    }

    public void update(
        String nome,
        LocalDate dataNascimento,
        String telefone,
        String email
    ) {
        this.nome = validateName(nome);
        this.dataNascimento = dataNascimento;
        this.telefone = validateContact(telefone);
        this.email = new Email(email);
    }

    private String validateName(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do responsável é obrigatório");
        }
        return nome;
    }

    private String validateContact(String telefone) {
        var normalized = MapperUtils.normalizeContact(telefone);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("Contato do responsável é obrigatório");
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

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public void setCpf(Cpf cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}

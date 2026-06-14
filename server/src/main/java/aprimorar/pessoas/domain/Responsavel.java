package aprimorar.pessoas.domain;

import aprimorar.shared.MapperUtils;
import jakarta.persistence.Column;
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

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_id", unique = true)
    private UUID userId;

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
        this.cpf = validateCpf(cpf);
        this.email = validateEmail(email);
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
        this.email = validateEmail(email);
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

    private String validateCpf(String cpf) {
        var normalized = MapperUtils.normalizeCpf(cpf);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("CPF do responsável é obrigatório");
        }
        return normalized;
    }

    private String validateEmail(String email) {
        var normalized = MapperUtils.normalizeEmail(email);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("Email do responsável é obrigatório");
        }
        return normalized;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

	public UUID getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public String getTelefone() {
		return telefone;
	}

	public UUID getUserId() {
		return userId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

}

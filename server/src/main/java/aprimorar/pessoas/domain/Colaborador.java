package aprimorar.pessoas.domain;

import aprimorar.pessoas.shared.FuncoesColaborador;
import aprimorar.pessoas.shared.Cpf;
import aprimorar.pessoas.shared.Email;
import aprimorar.shared.MapperUtils;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "colaboradores")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cpf", nullable = false, unique = true))
    private Cpf cpf;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private Email email;

    @Column(name = "pix", nullable = false)
    private String pix;

    @Column(name = "funcao", nullable = false)
    @Enumerated(EnumType.STRING)
    private FuncoesColaborador funcao;

    @Column(name = "ativo", nullable = false)
    private Boolean active = true;

    @Column(name = "user_id", unique = true)
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Embedded
    private Endereco endereco;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    protected Colaborador() {
    }

    public Colaborador(
            String name,
            LocalDate dataNascimento,
            String pix,
            String telefone,
            String cpf,
            String email,
            FuncoesColaborador funcao,
            Endereco endereco
    ) {

        this.nome = validateName(name);
        this.dataNascimento = validateDataNascimento(dataNascimento);
        this.pix = validatePix(pix);
        this.telefone = validateTelefone(telefone);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
        this.endereco = validateEndereco(endereco);
        this.funcao = funcao;
    }

    public void update(
            String name,
            LocalDate dataNascimento,
            String pix,
            String telefone,
            String email,
            FuncoesColaborador funcao,
            Endereco endereco
    ) {
        validateNotSystemRecord(this);
        this.nome = validateName(name);
        this.dataNascimento = validateDataNascimento(dataNascimento);
        this.pix = validatePix(pix);
        this.telefone = validateTelefone(telefone);
        this.email = new Email(email);
        this.funcao = funcao;
        this.endereco = validateEndereco(endereco);
    }

    public boolean isSystemRecord() {
        return FuncoesColaborador.SISTEMA.equals(this.funcao);
    }

    public void archive() {
        validateNotSystemRecord(this);
        this.active = false;
    }

    private static String validatePix(String pix) {
        if (pix == null || pix.isBlank()) {
            throw new IllegalArgumentException("Pix do colaborador é obrigatório");
        }
        return pix;
    }

    public void unarchive() {
        validateNotSystemRecord(this);
        this.active = true;
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do colaborador é obrigatório");
        }
        return name;
    }

    private LocalDate validateDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento do colaborador é obrigatória");
        }
        return dataNascimento;
    }

    private String validateTelefone(String telefone) {
        var normalized = MapperUtils.normalizeContact(telefone);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("Contato do colaborador é obrigatório");
        }
        return normalized;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    private Endereco validateEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço do colaborador é obrigatório");
        }
        return endereco;
    }

    private void validateNotSystemRecord(Colaborador colaborador) {
        if (colaborador.isSystemRecord()) {
            throw new IllegalArgumentException("Não é possível realizar esta operação em um registro do sistema.");
        }
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

	public String getCpf() {
		return cpf.value();
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

	public String getEmail() {
		return email.value();
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public String getPix() {
		return pix;
	}

	public void setPix(String pix) {
		this.pix = pix;
	}

	public FuncoesColaborador getFuncao() {
		return funcao;
	}

	public void setFuncao(FuncoesColaborador funcao) {
		this.funcao = funcao;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}



}

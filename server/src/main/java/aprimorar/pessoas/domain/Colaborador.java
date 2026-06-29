package aprimorar.pessoas.domain;

import aprimorar.pessoas.shared.FuncoesColaborador;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "pix", nullable = false)
    private String pix;

    @Column(name = "funcao", nullable = false)
    @Enumerated(EnumType.STRING)
    private FuncoesColaborador funcao;

    @Column(name = "ativo", nullable = false)
    private Boolean active = true;

    @Column(name = "user_id", unique = true)
    private UUID userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", nullable = false, unique = true)
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
        this.nome = name;
        this.dataNascimento = dataNascimento;
        this.pix = pix;
        this.telefone = telefone;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
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
        this.nome = name;
        this.dataNascimento = dataNascimento;
        this.pix = pix;
        this.telefone = telefone;
        this.email = email;
        this.funcao = funcao;
        this.endereco = endereco;
    }

    public boolean isSystemRecord() {
        return FuncoesColaborador.SISTEMA.equals(this.funcao);
    }

    public void archive() {
        validateNotSystemRecord(this);
        this.active = false;
    }

    public void unarchive() {
        validateNotSystemRecord(this);
        this.active = true;
    }

    public Endereco getEndereco() {
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
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
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

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
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

package aprimorar.pessoas.domain;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "escola")
    private String escola;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsavel_id", nullable = false)
    private Responsavel responsavel;

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

    protected Aluno() {
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Aluno(
            String nome,
            LocalDate dataNascimento,
            String telefone,
            String cpf,
            String email,
            String escola,
            Responsavel responsavel,
            Endereco endereco
    ) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.cpf = cpf;
        this.email = email;
        this.escola = escola;
        this.responsavel = responsavel;
        this.endereco = endereco;
    }

    public void update(
        String nome,
        LocalDate dataNascimento,
        String telefone,
        String email,
        String escola,
        Responsavel responsavel,
        Endereco endereco
    ) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.escola = escola;
        this.responsavel = responsavel;
        this.endereco = endereco;
    }

    @Transient
    public UUID getResponsavelId() {
        return responsavel.getId();
    }

    public void archive() {
        this.active = false;
    }

    public void unarchive() {
        this.active = true;
    }
}

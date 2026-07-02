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
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
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

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "pix", nullable = false)
    private String pix;

    @Column(name = "funcao", nullable = false, length = 100)
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
        this.nome = name;
        this.dataNascimento = dataNascimento;
        this.pix = pix;
        this.telefone = telefone;
        this.email = email;
        this.funcao = funcao;
        this.endereco = endereco;
    }

    public void archive() {
        this.active = false;
    }

    public void unarchive() {
        this.active = true;
    }
}

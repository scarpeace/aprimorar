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
@Table(name = "alunos")
public class Aluno {

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

    @Column(name = "escola")
    private String escola;

    @Column(name = "responsavel_id", nullable = false)
    private UUID responsavelId;

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
            UUID responsavelId,
            Endereco endereco
    ) {
        validateRequiredFields(endereco, responsavelId);
        this.nome = validateNome(nome);
        this.dataNascimento = validateDataNascimento(dataNascimento);
        this.telefone = validateTelefone(telefone);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
        this.escola = escola;
        this.responsavelId = responsavelId;
        this.endereco = endereco;
    }

    public void update(
        String nome,
        LocalDate dataNascimento,
        String telefone,
        String email,
        String escola,
        UUID responsavelId,
        Endereco endereco
    ) {
        validateRequiredFields(endereco, responsavelId);
        this.nome = validateNome(nome);
        this.dataNascimento = validateDataNascimento(dataNascimento);
        this.telefone = validateTelefone(telefone);
        this.email = new Email(email);
        this.escola = escola;
        this.responsavelId = responsavelId;
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

    private void validateRequiredFields(Endereco endereco, UUID responsavelId) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço do aluno é obrigatório");
        }
        if (responsavelId == null) {
            throw new IllegalArgumentException("Aluno não pode ser cadastrado sem um responsável");
        }
    }

    private String validateNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do aluno é obrigatório");
        }
        return nome;
    }

    private LocalDate validateDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento do aluno é obrigatória");
        }
        return dataNascimento;
    }

    private String validateTelefone(String telefone) {
        var normalized = MapperUtils.normalizeContact(telefone);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("Contato do aluno é obrigatório");
        }
        return normalized;
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

    public String getEscola() {
        return escola;
    }

    public void setEscola(String escola) {
        this.escola = escola;
    }

    public UUID getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(UUID responsavelId) {
        this.responsavelId = responsavelId;
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

package aprimorar.financeiro.recebimentos_alunos.domain;

import aprimorar.financeiro.recebimentos_alunos.domain.enums.StatusCobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaDadosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "cobrancas")
public class Cobranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atendimento_id", nullable = false, unique = true)
    private Long atendimentoId;

    @Column(name = "aluno_id", nullable = false)
    private UUID alunoId;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusCobranca status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recebimento_id")
    private CobrancaRecebimento recebimento;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Cobranca() {
    }

    public Cobranca(Long atendimentoId, UUID alunoId, BigDecimal valor) {
        this.atendimentoId = atendimentoId;
        this.alunoId = alunoId;
        this.valor = valor;
        this.status = StatusCobranca.PENDENTE;
    }

    private void validarDisponivelParaRecebimento() {
        if (recebimento != null || status == StatusCobranca.PAGA) {
            throw new CobrancaDadosInvalidosException("Cobrança já está paga");
        }

        if (
            status != StatusCobranca.PENDENTE
                && status != StatusCobranca.ATRASADA
        ) {
            throw new CobrancaDadosInvalidosException(
                "Somente cobranças pendentes ou atrasadas podem ser recebidas"
            );
        }
    }

    public void vincularRecebimento(CobrancaRecebimento recebimento) {
        if (recebimento == null) {
            throw new CobrancaDadosInvalidosException("Recebimento é obrigatório");
        }

        validarDisponivelParaRecebimento();

        this.recebimento = recebimento;
        this.status = StatusCobranca.PAGA;
    }

    public void desvincularRecebimento() {
        if (status != StatusCobranca.PAGA || recebimento == null) {
            throw new CobrancaDadosInvalidosException("Cobrança não possui recebimento");
        }

        this.recebimento = null;
        this.status = StatusCobranca.PENDENTE;
    }

    public void atualizar(UUID alunoId, BigDecimal valor) {
        if (status == StatusCobranca.PAGA || recebimento != null) {
            throw new CobrancaDadosInvalidosException(
                "Não é possível alterar uma cobrança já paga"
            );
        }
        if (status == StatusCobranca.CANCELADA) {
            throw new CobrancaDadosInvalidosException(
                "Não é possível alterar uma cobrança cancelada"
            );
        }

        this.alunoId = alunoId;
        this.valor = valor;
    }

    public void cancelar() {
        if (status == StatusCobranca.PAGA || recebimento != null) {
            throw new CobrancaDadosInvalidosException("Cobrança já está paga");
        }
        if (status == StatusCobranca.CANCELADA) {
            throw new CobrancaDadosInvalidosException("Cobrança já está cancelada");
        }

        this.status = StatusCobranca.CANCELADA;
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

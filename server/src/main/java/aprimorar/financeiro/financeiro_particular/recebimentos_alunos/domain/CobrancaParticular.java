package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain;

import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.enums.StatusCobrancaParticular;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CobrancaParticularDadosInvalidosException;
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
@Table(name = "cobrancas_particular")
public class CobrancaParticular {

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
    private StatusCobrancaParticular status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recebimento_id")
    private RecebimentoParticular recebimento;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected CobrancaParticular() {
    }

    public CobrancaParticular(Long atendimentoId, UUID alunoId, BigDecimal valor) {
        this.atendimentoId = atendimentoId;
        this.alunoId = alunoId;
        this.valor = valor;
        this.status = StatusCobrancaParticular.PENDENTE;
    }

    public StatusCobrancaParticular statusAtual() {
        if (
            status == StatusCobrancaParticular.PENDENTE
                && recebimento == null
                && createdAt != null
                && createdAt.isBefore(LocalDateTime.now().minusDays(30))
        ) {
            return StatusCobrancaParticular.ATRASADA;
        }

        return status;
    }

    private void validarDisponivelParaRecebimento() {
        StatusCobrancaParticular statusAtual = statusAtual();

        if (recebimento != null || statusAtual == StatusCobrancaParticular.PAGA) {
            throw new CobrancaParticularDadosInvalidosException("Cobrança já está paga");
        }

        if (
            statusAtual != StatusCobrancaParticular.PENDENTE
                && statusAtual != StatusCobrancaParticular.ATRASADA
        ) {
            throw new CobrancaParticularDadosInvalidosException(
                "Somente cobranças pendentes ou atrasadas podem ser recebidas"
            );
        }
    }

    public void vincularRecebimento(RecebimentoParticular recebimento) {
        if (recebimento == null) {
            throw new CobrancaParticularDadosInvalidosException("Recebimento é obrigatório");
        }

        validarDisponivelParaRecebimento();

        this.recebimento = recebimento;
        this.status = StatusCobrancaParticular.PAGA;
    }

    public void desvincularRecebimento() {
        if (status != StatusCobrancaParticular.PAGA || recebimento == null) {
            throw new CobrancaParticularDadosInvalidosException("Cobrança não possui recebimento");
        }

        this.recebimento = null;
        this.status = StatusCobrancaParticular.PENDENTE;
    }

    public void atualizar(UUID alunoId, BigDecimal valor) {
        if (status == StatusCobrancaParticular.PAGA || recebimento != null) {
            throw new CobrancaParticularDadosInvalidosException(
                "Não é possível alterar uma cobrança já paga"
            );
        }
        if (status == StatusCobrancaParticular.CANCELADA) {
            throw new CobrancaParticularDadosInvalidosException(
                "Não é possível alterar uma cobrança cancelada"
            );
        }

        this.alunoId = alunoId;
        this.valor = valor;
    }

    public void cancelar() {
        if (status == StatusCobrancaParticular.PAGA || recebimento != null) {
            throw new CobrancaParticularDadosInvalidosException("Cobrança já está paga");
        }
        if (status == StatusCobrancaParticular.CANCELADA) {
            throw new CobrancaParticularDadosInvalidosException("Cobrança já está cancelada");
        }

        this.status = StatusCobrancaParticular.CANCELADA;
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

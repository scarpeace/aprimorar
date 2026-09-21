package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain;

import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.enums.StatusRepasseParticular;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api.RepasseParticularDadosInvalidosException;
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
@Table(name = "repasses_particular")
public class RepasseParticular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atendimento_id", nullable = false, unique = true)
    private Long atendimentoId;

    @Column(name = "colaborador_id", nullable = false)
    private UUID colaboradorId;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusRepasseParticular status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id")
    private PagamentoParticular pagamento;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected RepasseParticular() {
    }

    public RepasseParticular(Long atendimentoId, UUID colaboradorId, BigDecimal valor) {
        this.atendimentoId = atendimentoId;
        this.colaboradorId = colaboradorId;
        this.valor = valor;
        this.status = StatusRepasseParticular.PENDENTE;
    }

    public StatusRepasseParticular statusAtual() {
        if (status == StatusRepasseParticular.PENDENTE
                && pagamento == null
                && createdAt != null
                && createdAt.isBefore(LocalDateTime.now().minusDays(30))
        ) {
            return StatusRepasseParticular.ATRASADO;
        }

        return status;
    }

    private void validarDisponivelParaPagamento() {
        StatusRepasseParticular statusAtual = statusAtual();

        if (pagamento != null || statusAtual == StatusRepasseParticular.PAGO) {
            throw new RepasseParticularDadosInvalidosException("Repasse já está pago");
        }

        if (
            statusAtual != StatusRepasseParticular.PENDENTE
                && statusAtual != StatusRepasseParticular.ATRASADO
        ) {
            throw new RepasseParticularDadosInvalidosException("Somente repasses pendentes ou atrasados podem ser pagos");
        }
    }

    public void vincularPagamento(PagamentoParticular pagamento) {
        validarDisponivelParaPagamento();

        this.pagamento = pagamento;
        this.status = StatusRepasseParticular.PAGO;
    }

    public void desvincularPagamento() {
        if (status != StatusRepasseParticular.PAGO || pagamento == null) {
            throw new RepasseParticularDadosInvalidosException("Repasse não possui pagamento");
        }

        this.pagamento = null;
        this.status = StatusRepasseParticular.PENDENTE;
    }

    public void atualizar(UUID colaboradorId, BigDecimal valor) {
        if (status == StatusRepasseParticular.PAGO || pagamento != null) {
            throw new RepasseParticularDadosInvalidosException("Não é possível alterar um repasse já pago");
        }
        if (status == StatusRepasseParticular.CANCELADO) {
            throw new RepasseParticularDadosInvalidosException("Não é possível alterar um repasse cancelado");
        }

        this.colaboradorId = colaboradorId;
        this.valor = valor;
    }

    public void cancelar() {
        if (status == StatusRepasseParticular.PAGO || pagamento != null) {
            throw new RepasseParticularDadosInvalidosException("Repasse já está pago");
        }
        if (status == StatusRepasseParticular.CANCELADO) {
            throw new RepasseParticularDadosInvalidosException("Repasse já está cancelado");
        }

        this.status = StatusRepasseParticular.CANCELADO;
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

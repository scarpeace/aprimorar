package aprimorar.financeiro.repasses_colaboradores.domain;

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

import aprimorar.financeiro.repasses_colaboradores.domain.enums.StatusRepasse;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseDadosInvalidosException;
import lombok.Getter;

@Getter
@Entity
@Table(name = "repasses")
public class Repasse {

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
    private StatusRepasse status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id")
    private RepassePagamento pagamento;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Repasse() {
    }

    public Repasse(Long atendimentoId, UUID colaboradorId, BigDecimal valor) {
        this.atendimentoId = atendimentoId;
        this.colaboradorId = colaboradorId;
        this.valor = valor;
        this.status = StatusRepasse.PENDENTE;
    }

    private void validarDisponivelParaPagamento() {
        if (pagamento != null || status == StatusRepasse.PAGO) {
            throw new RepasseDadosInvalidosException("Repasse já está pago");
        }

        if (
            status != StatusRepasse.PENDENTE
                && status != StatusRepasse.ATRASADO
        ) {
            throw new RepasseDadosInvalidosException("Somente repasses pendentes ou atrasados podem ser pagos");
        }
    }

    public void vincularPagamento(RepassePagamento pagamento) {
        validarDisponivelParaPagamento();

        this.pagamento = pagamento;
        this.status = StatusRepasse.PAGO;
    }

    public void desvincularPagamento() {
        if (status != StatusRepasse.PAGO || pagamento == null) {
            throw new RepasseDadosInvalidosException("Repasse não possui pagamento");
        }

        this.pagamento = null;
        this.status = StatusRepasse.PENDENTE;
    }

    public void atualizar(UUID colaboradorId, BigDecimal valor) {
        if (status == StatusRepasse.PAGO || pagamento != null) {
            throw new RepasseDadosInvalidosException("Não é possível alterar um repasse já pago");
        }
        if (status == StatusRepasse.CANCELADO) {
            throw new RepasseDadosInvalidosException("Não é possível alterar um repasse cancelado");
        }

        this.colaboradorId = colaboradorId;
        this.valor = valor;
    }

    public void cancelar() {
        if (status == StatusRepasse.PAGO || pagamento != null) {
            throw new RepasseDadosInvalidosException("Repasse já está pago");
        }
        if (status == StatusRepasse.CANCELADO) {
            throw new RepasseDadosInvalidosException("Repasse já está cancelado");
        }

        this.status = StatusRepasse.CANCELADO;
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

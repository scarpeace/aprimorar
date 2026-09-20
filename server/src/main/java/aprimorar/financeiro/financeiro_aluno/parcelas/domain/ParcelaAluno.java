package aprimorar.financeiro.financeiro_aluno.parcelas.domain;

import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.CobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.PagamentoAluno;
import aprimorar.financeiro.financeiro_aluno.parcelas.domain.enums.StatusParcelaAluno;
import aprimorar.financeiro.financeiro_aluno.parcelas.domain.exception.ParcelaAlunoDadosInvalidosException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "parcelas_alunos")
public class ParcelaAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cobranca_id", nullable = false)
    private CobrancaAluno cobranca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id")
    private PagamentoAluno pagamento;

    @Column(name = "numero_parcela", nullable = false)
    private Integer numeroParcela;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusParcelaAluno status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected ParcelaAluno() {}

    public ParcelaAluno(
        CobrancaAluno cobranca,
        Integer numeroParcela,
        BigDecimal valor,
        LocalDate dataVencimento
    ) {
        if (cobranca == null) {
            throw new ParcelaAlunoDadosInvalidosException("Cobrança da parcela é obrigatória");
        }
        if (numeroParcela == null || numeroParcela < 1) {
            throw new ParcelaAlunoDadosInvalidosException(
                "O número da parcela deve ser maior que zero"
            );
        }
        validarValor(valor);

        this.cobranca = cobranca;
        this.numeroParcela = numeroParcela;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.status = StatusParcelaAluno.PENDENTE;
    }

    public void registrarPagamento(PagamentoAluno pagamento) {
        if (status == StatusParcelaAluno.PAGA) {
            throw new ParcelaAlunoDadosInvalidosException("Parcela já está paga");
        }
        if (status == StatusParcelaAluno.CANCELADA) {
            throw new ParcelaAlunoDadosInvalidosException(
                "Não é possível registrar pagamento de uma parcela cancelada"
            );
        }
        if (pagamento == null) {
            throw new ParcelaAlunoDadosInvalidosException("Pagamento é obrigatório");
        }
        if (!cobranca.getAlunoId().equals(pagamento.getAlunoId())) {
            throw new ParcelaAlunoDadosInvalidosException(
                "O pagamento precisa pertencer ao mesmo aluno da parcela"
            );
        }

        this.pagamento = pagamento;
        this.status = StatusParcelaAluno.PAGA;
    }

    public void cancelarPagamento() {
        if (status != StatusParcelaAluno.PAGA) {
            throw new ParcelaAlunoDadosInvalidosException(
                "A parcela precisa estar paga para o pagamento ser cancelado"
            );
        }

        this.pagamento = null;
        this.status = StatusParcelaAluno.PENDENTE;
    }

    public void cancelarPorCobranca() {
        if (status != StatusParcelaAluno.PENDENTE) {
            throw new ParcelaAlunoDadosInvalidosException(
                "A parcela precisa estar pendente para ser cancelada"
            );
        }

        this.status = StatusParcelaAluno.CANCELADA;
    }

    public void atualizarValor(BigDecimal valor) {
        validarValor(valor);

        if (status != StatusParcelaAluno.PENDENTE) {
            throw new ParcelaAlunoDadosInvalidosException(
                "Não é possível alterar uma parcela que não está pendente"
            );
        }

        this.valor = valor;
    }

    public boolean estaPaga() {
        return status == StatusParcelaAluno.PAGA;
    }

    public StatusParcelaAluno getStatusAtual() {
        if (
            status == StatusParcelaAluno.PENDENTE
                && dataVencimento != null
                && dataVencimento.isBefore(LocalDate.now())
        ) {
            return StatusParcelaAluno.ATRASADA;
        }

        return status;
    }

    private static void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParcelaAlunoDadosInvalidosException(
                "O valor da parcela deve ser maior que zero"
            );
        }
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

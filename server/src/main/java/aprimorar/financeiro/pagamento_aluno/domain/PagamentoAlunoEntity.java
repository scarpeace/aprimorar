package aprimorar.financeiro.pagamento_aluno.domain;

import aprimorar.financeiro.pagamento_aluno.api.FormaPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "pagamentos_alunos")
public class PagamentoAlunoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atendimento_id")
    private Long atendimentoId;

    @Column(name = "aluno_id")
    private UUID alunoId;

    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusPagamentoAluno status;

    @Column(name = "lote")
    private Long lote;

    @Column(name = "comprovante_url", length = 500)
    private String comprovanteUrl;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "desconto", precision = 10, scale = 2)
    private BigDecimal desconto;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", length = 40)
    private FormaPagamento formaPagamento;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected PagamentoAlunoEntity() {}

    public PagamentoAlunoEntity(Long atendimentoId, UUID alunoId, BigDecimal valor) {
        validarValor(valor);
        this.atendimentoId = atendimentoId;
        this.alunoId = alunoId;
        this.valor = valor;
        this.status = StatusPagamentoAluno.PENDENTE;
    }

    public PagamentoAlunoEntity(
        LocalDateTime dataPagamento,
        BigDecimal total,
        BigDecimal desconto,
        FormaPagamento formaPagamento
    ) {
        validarCamposObrigatorios(dataPagamento, total, desconto, formaPagamento);
        this.dataPagamento = dataPagamento;
        this.valor = total;
        this.desconto = desconto;
        this.formaPagamento = formaPagamento;
        this.status = StatusPagamentoAluno.PAGO;
    }

    public void pagar(Long lote, BigDecimal desconto, FormaPagamento formaPagamento, String comprovanteUrl) {
        if (status == StatusPagamentoAluno.PAGO) {
            throw new IllegalStateException("Pagamento já está pago");
        }
        if (lote == null) {
            throw new IllegalArgumentException("Lote do pagamento é obrigatório");
        }
        validarFormaPagamento(formaPagamento);
        validarDesconto(desconto, valor);

        this.desconto = desconto;
        this.formaPagamento = formaPagamento;
        this.lote = lote;
        this.comprovanteUrl = comprovanteUrl;
        this.dataPagamento = LocalDateTime.now();
        this.status = StatusPagamentoAluno.PAGO;
    }

    public void cancelarPagamento() {
        this.status = StatusPagamentoAluno.PENDENTE;
        this.desconto = null;
        this.lote = null;
        this.dataPagamento = null;
        this.formaPagamento = null;
        this.comprovanteUrl = null;
    }

    private static void validarCamposObrigatorios(
        LocalDateTime dataPagamento,
        BigDecimal total,
        BigDecimal desconto,
        FormaPagamento formaPagamento
    ) {
        if (dataPagamento == null) {
            throw new IllegalArgumentException("Data de pagamento é obrigatória");
        }
        validarValor(total);
        validarFormaPagamento(formaPagamento);
        validarDesconto(desconto, total);
    }

    private static void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor do pagamento é obrigatório");
        }
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("Valor do pagamento deve ser positivo");
        }
    }

    private static void validarFormaPagamento(FormaPagamento formaPagamento) {
        if (formaPagamento == null) {
            throw new IllegalArgumentException("Forma de pagamento é obrigatória");
        }
    }

    private static void validarDesconto(BigDecimal desconto, BigDecimal valor) {
        if (desconto != null && desconto.signum() < 0) {
            throw new IllegalArgumentException("Desconto não pode ser negativo");
        }
        if (desconto != null && valor != null && desconto.compareTo(valor) > 0) {
            throw new IllegalArgumentException("Desconto não pode ser maior que o valor do pagamento");
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

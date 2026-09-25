package aprimorar.financeiro.operacional.domain;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

import aprimorar.financeiro.operacional.domain.enums.CategoriaDespesa;
import aprimorar.financeiro.operacional.domain.enums.FormaPagamento;
import aprimorar.financeiro.operacional.domain.enums.StatusDespesa;
import aprimorar.financeiro.operacional.domain.enums.TipoDespesa;
import aprimorar.financeiro.operacional.domain.exception.DespesaDadosInvalidosException;
import lombok.Getter;

@Getter
@Entity
@Table(name = "despesas")
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 120)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoDespesa tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 40)
    private CategoriaDespesa categoria;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusDespesa status;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 40)
    private FormaPagamento formaPagamento;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Despesa() {}

    public Despesa(
        String titulo,
        TipoDespesa tipo,
        CategoriaDespesa categoria,
        BigDecimal valor,
        LocalDate dataVencimento,
        FormaPagamento formaPagamento,
        String descricao
    ) {
        validarCamposObrigatorios(titulo, tipo, categoria, valor, dataVencimento, formaPagamento);
        this.titulo = titulo.trim();
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.status = StatusDespesa.PENDENTE;
        this.formaPagamento = formaPagamento;
        this.descricao = descricao;
    }

    public void update(
        String titulo,
        TipoDespesa tipo,
        CategoriaDespesa categoria,
        BigDecimal valor,
        LocalDate dataVencimento,
        FormaPagamento formaPagamento,
        String descricao
    ) {
        validarCamposObrigatorios(titulo, tipo, categoria, valor, dataVencimento, formaPagamento);
        this.titulo = titulo.trim();
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.formaPagamento = formaPagamento;
        this.descricao = descricao;
        if (dataPagamento != null) {
            this.status = StatusDespesa.PAGA;
        }
    }

    public void pagar() {
        this.dataPagamento = LocalDate.now();
        this.status = StatusDespesa.PAGA;
    }

    public void cancelarPagamento() {
        this.dataPagamento = null;
        this.status = StatusDespesa.PENDENTE;
    }

    private static void validarCamposObrigatorios(
        String titulo,
        TipoDespesa tipo,
        CategoriaDespesa categoria,
        BigDecimal valor,
        LocalDate dataVencimento,
        FormaPagamento formaPagamento
    ) {
        if (titulo == null || titulo.isBlank()) {
            throw new DespesaDadosInvalidosException("Título é obrigatório");
        }
        if (tipo == null) {
            throw new DespesaDadosInvalidosException("Tipo é obrigatório");
        }
        if (categoria == null) {
            throw new DespesaDadosInvalidosException("Categoria é obrigatória");
        }
        if (valor == null) {
            throw new DespesaDadosInvalidosException("Valor é obrigatório");
        }
        if (dataVencimento == null) {
            throw new DespesaDadosInvalidosException("Data de vencimento é obrigatória");
        }
        if (formaPagamento == null) {
            throw new DespesaDadosInvalidosException("Forma de pagamento é obrigatória");
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

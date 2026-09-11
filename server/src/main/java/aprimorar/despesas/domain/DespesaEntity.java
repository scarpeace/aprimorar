package aprimorar.despesas.domain;

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

import aprimorar.despesas.domain.enums.CategoriaDespesa;
import aprimorar.despesas.domain.enums.FormaPagamento;
import aprimorar.despesas.domain.enums.StatusDespesa;
import aprimorar.despesas.domain.enums.TipoDespesa;
import lombok.Getter;

@Getter
@Entity
@Table(name = "despesas")
public class DespesaEntity {

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

    protected DespesaEntity() {}

    public DespesaEntity(
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
        this.status = calcularStatus(dataVencimento);
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
        this.status = dataPagamento == null ? calcularStatus(dataVencimento) : StatusDespesa.PAGA;
    }

    public void pagar() {
        this.dataPagamento = LocalDate.now();
        this.status = StatusDespesa.PAGA;
    }

    public void cancelarPagamento() {
        this.dataPagamento = null;
        this.status = calcularStatus(dataVencimento);
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
            throw new IllegalArgumentException("Título é obrigatório");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo é obrigatório");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        if (valor == null) {
            throw new IllegalArgumentException("Valor é obrigatório");
        }
        if (dataVencimento == null) {
            throw new IllegalArgumentException("Data de vencimento é obrigatória");
        }
        if (formaPagamento == null) {
            throw new IllegalArgumentException("Forma de pagamento é obrigatória");
        }
    }

    private static StatusDespesa calcularStatus(LocalDate dataVencimento) {
        return dataVencimento.isBefore(LocalDate.now()) ? StatusDespesa.ATRASADA : StatusDespesa.PENDENTE;
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

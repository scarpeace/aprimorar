package aprimorar.despesas.domain;

import aprimorar.despesas.enums.CategoriaDespesa;
import aprimorar.despesas.enums.FormaPagamento;
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
    @Column(name = "categoria", nullable = false, length = 40)
    private CategoriaDespesa categoria;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDate dataPagamento;

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
        CategoriaDespesa categoria,
        BigDecimal valor,
        LocalDate dataPagamento,
        FormaPagamento formaPagamento,
        String descricao
    ) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
        this.descricao = descricao;
    }

    public void update(
        String titulo,
        CategoriaDespesa categoria,
        BigDecimal valor,
        LocalDate dataPagamento,
        FormaPagamento formaPagamento,
        String descricao
    ) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
        this.descricao = descricao;
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

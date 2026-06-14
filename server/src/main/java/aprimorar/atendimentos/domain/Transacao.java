package aprimorar.atendimentos.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import aprimorar.atendimentos.enums.CategoriaTransacao;
import aprimorar.atendimentos.enums.FormaPagamento;
import aprimorar.atendimentos.enums.StatusTransacao;
import aprimorar.atendimentos.enums.TipoTransacao;

@Getter
@Entity
@Table(name = "transacoes")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pagador_id", nullable = false)
    private UUID pagadorId;

    @Column(name = "recebedor_id", nullable = false)
    private UUID recebedorId;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_efetivada", nullable = false)
    private LocalDateTime dataEfetivada;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoTransacao tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 20)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusTransacao status;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaTransacao categoria;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    protected Transacao() {
    }

    public Transacao(
            UUID pagadorId,
            UUID recebedorID,
            BigDecimal valor,
            LocalDateTime dataEfetivada,
            TipoTransacao tipo,
            FormaPagamento formaPagamento,
            StatusTransacao status,
            CategoriaTransacao categoria
            ){
        this.pagadorId = pagadorId;
        this.recebedorId = recebedorID;
        this.valor = valor;
        this.dataEfetivada = dataEfetivada;
        this.tipo = tipo;
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.categoria = categoria;
    }

}

package aprimorar.atendimentos.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

    @OneToOne
    @JoinColumn(name = "atendimento_id")
    private Atendimento atendimento;

    @Column(name = "pagador_id", nullable = false)
    private UUID pagadorId;

    @Column(name = "recebedor_id", nullable = false)
    private UUID recebedorId;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_efetivada")
    private LocalDateTime dataEfetivada;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoTransacao tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", length = 20)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusTransacao status;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaTransacao categoria;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Transacao() {
    }

    private final UUID ADMIN_ID = UUID.fromString("b3a092e0-fc48-43ff-8b35-149eb81a033f");

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
        validateRequiredFields();
        validateValores();
        this.pagadorId = pagadorId;
        this.recebedorId = recebedorID;
        this.valor = valor;
        this.dataEfetivada = dataEfetivada;
        this.tipo = tipo;
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.categoria = categoria;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void efetivar(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
        this.status = StatusTransacao.PAGO;
        this.dataEfetivada = LocalDateTime.now();
    }

    public void cancelar() {
        this.status = StatusTransacao.CANCELADO;
    }

    private void validateValores() {
        if (this.valor == null || this.valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
    }

    private void validateRequiredFields() {
        if (this.pagadorId == null || this.recebedorId == null || this.tipo == null || this.formaPagamento == null || this.status == null || this.categoria == null) {
            throw new IllegalArgumentException("Campos obrigatórios não foram preenchidos");
        }
    }


}

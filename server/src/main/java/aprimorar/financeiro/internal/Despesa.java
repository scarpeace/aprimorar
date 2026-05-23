package aprimorar.financeiro.internal;

import aprimorar.financeiro.api.CategoriaDespesa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tb_despesas")
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "valor", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "data", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 50)
    private CategoriaDespesa category;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "data_pagamento")
    private Instant paymentDate;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private Instant updatedAt;

    protected Despesa() {}

    public Despesa(BigDecimal amount, LocalDate date, CategoriaDespesa category, String description) {
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public CategoriaDespesa getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void update(BigDecimal amount, LocalDate date, CategoriaDespesa category, String description) {
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.description = description;
    }

    public void togglePayment(Instant now) {
        if (this.paymentDate != null) {
            this.paymentDate = null;
        } else {
            this.paymentDate = now;
        }
    }

    @Transient
    public boolean isPaid() {
        return this.paymentDate != null;
    }
}

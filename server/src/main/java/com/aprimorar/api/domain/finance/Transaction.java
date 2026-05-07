package com.aprimorar.api.domain.finance;

import com.aprimorar.api.enums.TransactionCategory;
import com.aprimorar.api.enums.TransactionOrigin;
import com.aprimorar.api.enums.TransactionStatus;
import com.aprimorar.api.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tb_transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TransactionOrigin origin;

    @Column(name = "origin_id", nullable = false)
    private UUID originId;

    @Column(name = "settled_at")
    private Instant settledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TransactionCategory category;

    protected Transaction() {}

    public Transaction(
        TransactionType type,
        TransactionStatus status,
        BigDecimal amount,
        TransactionOrigin origin,
        UUID originId,
        Instant settledAt,
        TransactionCategory category
    ) {
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.origin = origin;
        this.originId = originId;
        this.settledAt = settledAt;
        this.category = category;
    }

    public UUID getId() {
        return id;
    }


    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionOrigin getOrigin() {
        return origin;
    }

    public UUID getOriginId() {
        return originId;
    }

    public Instant getSettledAt() {
        return settledAt;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setSettledAt(Instant settledAt) {
        this.settledAt = settledAt;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public void setOriginId(UUID originId) {
        this.originId = originId;
    }

    public void settle(Instant settledAt) {
        this.status = TransactionStatus.SETTLED;
        this.setSettledAt(settledAt);
    }

    public void reopen() {
        this.status = TransactionStatus.PENDING;
        this.setSettledAt(null);
    }
}

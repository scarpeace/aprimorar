package com.aprimorar.api.domain.finance;

import com.aprimorar.api.enums.GeneralExpenseCategory;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_general_expenses")
public class GeneralExpense extends BaseEntity {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private GeneralExpenseCategory category;

    protected GeneralExpense() {}

    public GeneralExpense(String description, BigDecimal amount, LocalDate date, GeneralExpenseCategory category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public GeneralExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(GeneralExpenseCategory category) {
        this.category = category;
    }
}

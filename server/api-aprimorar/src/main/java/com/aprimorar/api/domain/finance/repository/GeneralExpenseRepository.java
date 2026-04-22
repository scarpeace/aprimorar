package com.aprimorar.api.domain.finance.repository;

import com.aprimorar.api.domain.finance.GeneralExpense;
import com.aprimorar.api.enums.GeneralExpenseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface GeneralExpenseRepository extends JpaRepository<GeneralExpense, UUID> {

    @Query("SELECT g FROM GeneralExpense g " +
           "WHERE (:category IS NULL OR g.category = :category) " +
           "AND (:startDate IS NULL OR g.date >= :startDate) " +
           "AND (:endDate IS NULL OR g.date <= :endDate)")
    Page<GeneralExpense> findAllWithFilters(
            @Param("category") GeneralExpenseCategory category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query("SELECT COALESCE(SUM(g.amount), 0) FROM GeneralExpense g")
    java.math.BigDecimal sumTotalGeneralExpenses();
}

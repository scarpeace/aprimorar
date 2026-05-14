package aprimorar.expense.internal;

import aprimorar.expense.api.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    @Query(
        """
        select e
        from Expense e
        where (:category is null or e.category = :category)
          and e.date >= coalesce(:startDate, e.date)
          and e.date <= coalesce(:endDate, e.date)
        """
    )
    Page<Expense> findFiltered(
        @Param("category") ExpenseCategory category,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );

    @Query(
        """
        select coalesce(sum(e.amount), 0)
        from Expense e
        where e.date >= coalesce(:startDate, e.date)
          and e.date <= coalesce(:endDate, e.date)
        """
    )
    BigDecimal sumFiltered(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

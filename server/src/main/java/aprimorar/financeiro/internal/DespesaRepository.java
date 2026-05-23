package aprimorar.financeiro.internal;

import aprimorar.financeiro.api.CategoriaDespesa;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DespesaRepository extends JpaRepository<Despesa, UUID> {

    @Query(
        """
        select e
        from Despesa e
        where (:category is null or e.category = :category)
          and e.date >= coalesce(:startDate, e.date)
          and e.date <= coalesce(:endDate, e.date)
        """
    )
    Page<Despesa> findFiltered(
        @Param("category") CategoriaDespesa category,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );

    @Query(
        """
        select coalesce(sum(e.amount), 0)
        from Despesa e
        where e.date >= coalesce(:startDate, e.date)
          and e.date <= coalesce(:endDate, e.date)
        """
    )
    BigDecimal sumFiltered(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(
        """
        select coalesce(sum(e.amount), 0)
        from Despesa e
        where e.date >= coalesce(:startDate, e.date)
          and e.date <= coalesce(:endDate, e.date)
          and e.paymentDate is null
        """
    )
    BigDecimal sumPendingFiltered(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

package aprimorar.financeiro.internal.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import aprimorar.financeiro.internal.domain.Despesa;

public interface DespesaRepository extends JpaRepository<Despesa, UUID>, JpaSpecificationExecutor<Despesa>, DespesaRepositoryCustom {

    @Query(
        """
        select coalesce(sum(e.amount), 0)
        from Despesa e
        where e.date >= coalesce(:startDate, e.date)
          and e.date <= coalesce(:endDate, e.date)
        """
    )
    BigDecimal sumFiltered(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        select coalesce(sum(e.amount), 0)
        from Despesa e
        where e.date >= coalesce(:startDate, e.date)
          and e.date <= coalesce(:endDate, e.date)
          and e.paymentDate is null
        """
    )
    BigDecimal sumPendingFiltered(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
}

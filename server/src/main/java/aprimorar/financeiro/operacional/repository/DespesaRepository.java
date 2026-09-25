package aprimorar.financeiro.operacional.repository;

import aprimorar.financeiro.operacional.domain.Despesa;
import aprimorar.financeiro.operacional.domain.enums.StatusDespesa;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DespesaRepository extends JpaRepository<Despesa, Long>, JpaSpecificationExecutor<Despesa> {

    @Modifying
    @Query("""
        update Despesa d
           set d.status = :statusDestino,
               d.updatedAt = CURRENT_TIMESTAMP
         where d.status = :statusOrigem
           and d.dataPagamento is null
           and d.dataVencimento < :hoje
        """)
    int marcarAtrasadas(
        @Param("statusOrigem") StatusDespesa statusOrigem,
        @Param("statusDestino") StatusDespesa statusDestino,
        @Param("hoje") LocalDate hoje
    );

    @Modifying
    @Query("""
        update Despesa d
           set d.status = :statusDestino,
               d.updatedAt = CURRENT_TIMESTAMP
         where d.status = :statusOrigem
           and d.dataPagamento is null
           and d.dataVencimento >= :hoje
        """)
    int reabrirVencimentosFuturos(
        @Param("statusOrigem") StatusDespesa statusOrigem,
        @Param("statusDestino") StatusDespesa statusDestino,
        @Param("hoje") LocalDate hoje
    );
}

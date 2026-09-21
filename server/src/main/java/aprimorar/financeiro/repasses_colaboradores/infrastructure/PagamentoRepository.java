package aprimorar.financeiro.repasses_colaboradores.infrastructure;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import aprimorar.financeiro.repasses_colaboradores.domain.RepassePagamento;

public interface PagamentoRepository
    extends JpaRepository<RepassePagamento, UUID>,
        JpaSpecificationExecutor<RepassePagamento> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from RepassePagamento p where p.id = :pagamentoId")
    Optional<RepassePagamento> findByIdForUpdate(
        @Param("pagamentoId") UUID pagamentoId
    );

    @EntityGraph(attributePaths = "repasses")
    @Query("select p from RepassePagamento p where p.id = :pagamentoId")
    Optional<RepassePagamento> findByIdWithRepasses(
        @Param("pagamentoId") UUID pagamentoId
    );
}

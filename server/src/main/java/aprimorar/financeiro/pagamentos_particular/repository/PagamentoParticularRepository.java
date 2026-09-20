package aprimorar.financeiro.pagamentos_particular.repository;

import aprimorar.financeiro.pagamentos_particular.domain.PagamentoParticular;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PagamentoParticularRepository
    extends JpaRepository<PagamentoParticular, UUID>,
        JpaSpecificationExecutor<PagamentoParticular> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PagamentoParticular p where p.id = :pagamentoId")
    Optional<PagamentoParticular> findByIdForUpdate(
        @Param("pagamentoId") UUID pagamentoId
    );

    @EntityGraph(attributePaths = "repasses")
    @Query("select p from PagamentoParticular p where p.id = :pagamentoId")
    Optional<PagamentoParticular> findByIdWithRepasses(
        @Param("pagamentoId") UUID pagamentoId
    );
}

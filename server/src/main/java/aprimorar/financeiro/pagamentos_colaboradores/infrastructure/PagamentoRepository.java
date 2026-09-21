package aprimorar.financeiro.pagamentos_colaboradores.infrastructure;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import aprimorar.financeiro.pagamentos_colaboradores.domain.Pagamento;

public interface PagamentoRepository
    extends JpaRepository<Pagamento, UUID>,
        JpaSpecificationExecutor<Pagamento> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PagamentoParticular p where p.id = :pagamentoId")
    Optional<Pagamento> findByIdForUpdate(
        @Param("pagamentoId") UUID pagamentoId
    );

    @EntityGraph(attributePaths = "repasses")
    @Query("select p from PagamentoParticular p where p.id = :pagamentoId")
    Optional<Pagamento> findByIdWithRepasses(
        @Param("pagamentoId") UUID pagamentoId
    );
}

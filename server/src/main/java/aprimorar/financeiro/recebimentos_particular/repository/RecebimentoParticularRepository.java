package aprimorar.financeiro.recebimentos_particular.repository;

import aprimorar.financeiro.recebimentos_particular.domain.RecebimentoParticular;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecebimentoParticularRepository
    extends JpaRepository<RecebimentoParticular, UUID>, JpaSpecificationExecutor<RecebimentoParticular> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RecebimentoParticular r where r.id = :recebimentoId")
    Optional<RecebimentoParticular> findByIdForUpdate(
        @Param("recebimentoId") UUID recebimentoId
    );

    @EntityGraph(attributePaths = "cobrancas")
    @Query("select r from RecebimentoParticular r where r.id = :recebimentoId")
    Optional<RecebimentoParticular> findByIdWithCobrancas(
        @Param("recebimentoId") UUID recebimentoId
    );
}

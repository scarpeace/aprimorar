package aprimorar.financeiro.recebimentos_alunos.infrastructure;

import aprimorar.financeiro.recebimentos_alunos.domain.CobrancaRecebimento;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecebimentoRepository
    extends JpaRepository<CobrancaRecebimento, UUID>, JpaSpecificationExecutor<CobrancaRecebimento> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from CobrancaRecebimento r where r.id = :recebimentoId")
    Optional<CobrancaRecebimento> findByIdForUpdate(
        @Param("recebimentoId") UUID recebimentoId
    );

    @EntityGraph(attributePaths = "cobrancas")
    @Query("select r from CobrancaRecebimento r where r.id = :recebimentoId")
    Optional<CobrancaRecebimento> findByIdWithCobrancas(
        @Param("recebimentoId") UUID recebimentoId
    );
}

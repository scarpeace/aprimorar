package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.repository;

import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.RepasseParticular;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.enums.StatusRepasseParticular;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepasseParticularRepository extends JpaRepository<RepasseParticular, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RepasseParticular r where r.id in :ids")
    List<RepasseParticular> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RepasseParticular r where r.pagamento.id = :pagamentoId")
    List<RepasseParticular> findAllByPagamentoIdForUpdate(@Param("pagamentoId") UUID pagamentoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RepasseParticular r where r.atendimentoId = :atendimentoId")
    Optional<RepasseParticular> findByAtendimentoIdForUpdate(
        @Param("atendimentoId") Long atendimentoId
    );

    Optional<RepasseParticular> findByAtendimentoId(Long atendimentoId);

    boolean existsByColaboradorIdAndStatus(UUID colaboradorId, StatusRepasseParticular status);

    List<RepasseParticular> findAllByAtendimentoIdIn(Collection<Long> atendimentoIds);
}

package aprimorar.financeiro.pagamentos_colaboradores.infrastructure;

import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import aprimorar.financeiro.pagamentos_colaboradores.domain.Repasse;
import aprimorar.financeiro.pagamentos_colaboradores.domain.enums.StatusRepasse;

public interface RepasseRepository
    extends JpaRepository<Repasse, Long>, JpaSpecificationExecutor<Repasse> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RepasseParticular r where r.id in :ids")
    List<Repasse> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RepasseParticular r where r.pagamento.id = :pagamentoId")
    List<Repasse> findAllByPagamentoIdForUpdate(@Param("pagamentoId") UUID pagamentoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RepasseParticular r where r.atendimentoId = :atendimentoId")
    Optional<Repasse> findByAtendimentoIdForUpdate(
        @Param("atendimentoId") Long atendimentoId
    );

    Optional<Repasse> findByAtendimentoId(Long atendimentoId);

    boolean existsByColaboradorIdAndStatus(UUID colaboradorId, StatusRepasse status);

    List<Repasse> findAllByAtendimentoIdIn(Collection<Long> atendimentoIds);
}

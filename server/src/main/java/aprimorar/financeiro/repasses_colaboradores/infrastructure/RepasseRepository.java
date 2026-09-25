package aprimorar.financeiro.repasses_colaboradores.infrastructure;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import aprimorar.financeiro.repasses_colaboradores.domain.Repasse;
import aprimorar.financeiro.repasses_colaboradores.domain.enums.StatusRepasse;

public interface RepasseRepository
    extends JpaRepository<Repasse, Long>, JpaSpecificationExecutor<Repasse> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Repasse r where r.id in :ids")
    List<Repasse> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Repasse r where r.pagamento.id = :pagamentoId")
    List<Repasse> findAllByPagamentoIdForUpdate(@Param("pagamentoId") UUID pagamentoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Repasse r where r.atendimentoId = :atendimentoId")
    Optional<Repasse> findByAtendimentoIdForUpdate(
        @Param("atendimentoId") Long atendimentoId
    );

    Optional<Repasse> findByAtendimentoId(Long atendimentoId);

    boolean existsByColaboradorIdAndStatusIn(UUID colaboradorId, Collection<StatusRepasse> statuses);

    @Modifying
    @Query("""
        update Repasse r
           set r.status = :statusDestino,
               r.updatedAt = CURRENT_TIMESTAMP
         where r.status = :statusOrigem
           and r.pagamento is null
           and r.createdAt < :limite
        """)
    int marcarAtrasados(
        @Param("statusOrigem") StatusRepasse statusOrigem,
        @Param("statusDestino") StatusRepasse statusDestino,
        @Param("limite") LocalDateTime limite
    );

    List<Repasse> findAllByAtendimentoIdIn(Collection<Long> atendimentoIds);
}

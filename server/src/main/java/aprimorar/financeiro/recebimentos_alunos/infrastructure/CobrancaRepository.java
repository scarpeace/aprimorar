package aprimorar.financeiro.recebimentos_alunos.infrastructure;

import aprimorar.financeiro.recebimentos_alunos.domain.Cobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.enums.StatusCobranca;
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

public interface CobrancaRepository
    extends JpaRepository<Cobranca, Long>, JpaSpecificationExecutor<Cobranca> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cobranca c where c.id in :ids")
    List<Cobranca> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cobranca c where c.atendimentoId = :atendimentoId")
    Optional<Cobranca> findByAtendimentoIdForUpdate(
        @Param("atendimentoId") Long atendimentoId
    );

    Optional<Cobranca> findByAtendimentoId(Long atendimentoId);

    boolean existsByAlunoIdAndStatusIn(UUID alunoId, Collection<StatusCobranca> statuses);

    @Modifying
    @Query("""
        update Cobranca c
           set c.status = :statusDestino,
               c.updatedAt = CURRENT_TIMESTAMP
         where c.status = :statusOrigem
           and c.recebimento is null
           and c.createdAt < :limite
        """)
    int marcarAtrasadas(
        @Param("statusOrigem") StatusCobranca statusOrigem,
        @Param("statusDestino") StatusCobranca statusDestino,
        @Param("limite") LocalDateTime limite
    );

    List<Cobranca> findAllByAtendimentoIdIn(Collection<Long> atendimentoIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cobranca c where c.recebimento.id = :recebimentoId")
    List<Cobranca> findAllByRecebimentoIdForUpdate(
        @Param("recebimentoId") UUID recebimentoId
    );
}

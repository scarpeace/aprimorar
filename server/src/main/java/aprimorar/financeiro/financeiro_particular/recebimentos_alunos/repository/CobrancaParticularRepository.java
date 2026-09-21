package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.repository;

import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.CobrancaParticular;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.enums.StatusCobrancaParticular;
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

public interface CobrancaParticularRepository
    extends JpaRepository<CobrancaParticular, Long>, JpaSpecificationExecutor<CobrancaParticular> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CobrancaParticular c where c.id in :ids")
    List<CobrancaParticular> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CobrancaParticular c where c.atendimentoId = :atendimentoId")
    Optional<CobrancaParticular> findByAtendimentoIdForUpdate(
        @Param("atendimentoId") Long atendimentoId
    );

    Optional<CobrancaParticular> findByAtendimentoId(Long atendimentoId);

    boolean existsByAlunoIdAndStatus(UUID alunoId, StatusCobrancaParticular status);

    List<CobrancaParticular> findAllByAtendimentoIdIn(Collection<Long> atendimentoIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CobrancaParticular c where c.recebimento.id = :recebimentoId")
    List<CobrancaParticular> findAllByRecebimentoIdForUpdate(
        @Param("recebimentoId") UUID recebimentoId
    );
}

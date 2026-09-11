package aprimorar.atendimentos.individuais.repository.cobranca;

import aprimorar.atendimentos.individuais.domain.CobrancaIndividualEntity;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CobrancaIndividualRepository
    extends JpaRepository<CobrancaIndividualEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CobrancaIndividualEntity c where c.id in :ids")
    List<CobrancaIndividualEntity> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CobrancaIndividualEntity c where c.atendimentoId = :atendimentoId")
    Optional<CobrancaIndividualEntity> findByAtendimentoIdForUpdate(@Param("atendimentoId") Long atendimentoId);

}

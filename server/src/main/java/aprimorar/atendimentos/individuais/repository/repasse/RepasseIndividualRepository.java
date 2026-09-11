package aprimorar.atendimentos.individuais.repository.repasse;

import aprimorar.atendimentos.individuais.domain.RepasseIndividualEntity;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepasseIndividualRepository extends JpaRepository<RepasseIndividualEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RepasseIndividualEntity r where r.id in :ids")
    List<RepasseIndividualEntity> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RepasseIndividualEntity r where r.atendimentoId = :atendimentoId")
    Optional<RepasseIndividualEntity> findByAtendimentoIdForUpdate(@Param("atendimentoId") Long atendimentoId);
}

package aprimorar.financeiro.cobranca_aluno.repository;

import aprimorar.financeiro.cobranca_aluno.domain.CobrancaAlunoEntity;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CobrancaAlunoRepository
    extends JpaRepository<CobrancaAlunoEntity, Long>, JpaSpecificationExecutor<CobrancaAlunoEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CobrancaAlunoEntity c where c.id in :ids")
    List<CobrancaAlunoEntity> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

}

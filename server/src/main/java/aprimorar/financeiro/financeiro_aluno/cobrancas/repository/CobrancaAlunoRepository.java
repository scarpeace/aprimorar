package aprimorar.financeiro.financeiro_aluno.cobrancas.repository;

import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.CobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.enums.StatusCobrancaAluno;
import aprimorar.financeiro.api.financeiro_aluno.TipoOrigemCobrancaAluno;
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

public interface CobrancaAlunoRepository
    extends JpaRepository<CobrancaAluno, Long>, JpaSpecificationExecutor<CobrancaAluno> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CobrancaAluno c where c.id in :ids")
    List<CobrancaAluno> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CobrancaAluno c where c.origemId = :origemId and c.origemTipo = :origemTipo")
    Optional<CobrancaAluno> findByOrigemIdAndOrigemTipoForUpdate(
        @Param("origemId") Long origemId,
        @Param("origemTipo") TipoOrigemCobrancaAluno origemTipo
    );

    Optional<CobrancaAluno> findByOrigemIdAndOrigemTipo(
        Long origemId,
        TipoOrigemCobrancaAluno origemTipo
    );

    boolean existsByOrigemIdAndOrigemTipo(Long origemId, TipoOrigemCobrancaAluno origemTipo);

    boolean existsByAlunoIdAndStatusIn(UUID alunoId, Collection<StatusCobrancaAluno> statuses);

    List<CobrancaAluno> findAllByOrigemIdInAndOrigemTipo(
        Collection<Long> origemIds,
        TipoOrigemCobrancaAluno origemTipo
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select distinct c from CobrancaAluno c join c.parcelas p where p.pagamento.id = :pagamentoId")
    List<CobrancaAluno> findAllByPagamentoIdForUpdate(@Param("pagamentoId") UUID pagamentoId);

    @Query("select distinct c from CobrancaAluno c join c.parcelas p where p.pagamento.id = :pagamentoId order by c.id asc")
    List<CobrancaAluno> findAllByPagamentoIdOrderByIdAsc(UUID pagamentoId);

}

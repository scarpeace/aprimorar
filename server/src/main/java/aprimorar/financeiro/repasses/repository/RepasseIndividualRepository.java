package aprimorar.financeiro.repasses.repository;

import aprimorar.financeiro.repasses.domain.enums.StatusRepasseIndividual;
import aprimorar.financeiro.repasses.domain.RepasseIndividual;
import aprimorar.financeiro.repasses.repository.projections.RepasseLoteProjection;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepasseIndividualRepository
    extends JpaRepository<RepasseIndividual, Long>, JpaSpecificationExecutor<RepasseIndividual> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RepasseIndividual r where r.id in :ids")
    List<RepasseIndividual> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RepasseIndividual r where r.atendimentoId = :atendimentoId")
    Optional<RepasseIndividual> findByAtendimentoIdForUpdate(@Param("atendimentoId") Long atendimentoId);

    boolean existsByAtendimentoId(Long atendimentoId);

    boolean existsByColaboradorIdAndStatus(UUID colaboradorId, StatusRepasseIndividual status);

    List<RepasseIndividual> findAllByAtendimentoIdIn(Collection<Long> atendimentoIds);

    @Query(
        value = """
            select
                r.loteId as loteId,
                r.colaboradorId as colaboradorId,
                min(r.dataRepasse) as dataRepasse,
                r.formaPagamento as formaPagamento,
                r.comprovanteUrl as comprovanteUrl,
                sum(r.valor) as valorTotal,
                count(r.id) as quantidadeRepasses
            from RepasseIndividual r
            where r.colaboradorId = :colaboradorId
              and r.status = :status
              and r.loteId is not null
            group by
                r.loteId,
                r.colaboradorId,
                r.formaPagamento,
                r.comprovanteUrl
            order by min(r.dataRepasse) desc
            """,
        countQuery = """
            select count(distinct r.loteId)
            from RepasseIndividual r
            where r.colaboradorId = :colaboradorId
              and r.status = :status
              and r.loteId is not null
            """
    )
    Page<RepasseLoteProjection> findLotesPorColaboradorId(
        @Param("colaboradorId") UUID colaboradorId,
        @Param("status") StatusRepasseIndividual status,
        Pageable pageable
    );

    @Query(
        """
            select
                r.loteId as loteId,
                r.colaboradorId as colaboradorId,
                min(r.dataRepasse) as dataRepasse,
                r.formaPagamento as formaPagamento,
                r.comprovanteUrl as comprovanteUrl,
                sum(r.valor) as valorTotal,
                count(r.id) as quantidadeRepasses
            from RepasseIndividual r
            where r.loteId = :loteId
              and r.status = :status
            group by
                r.loteId,
                r.colaboradorId,
                r.formaPagamento,
                r.comprovanteUrl
            """
    )
    Optional<RepasseLoteProjection> findLotePorId(
        @Param("loteId") UUID loteId,
        @Param("status") StatusRepasseIndividual status
    );
}

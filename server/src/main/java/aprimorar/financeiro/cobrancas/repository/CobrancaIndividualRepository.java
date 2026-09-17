package aprimorar.financeiro.cobrancas.repository;

import aprimorar.financeiro.cobrancas.domain.CobrancaIndividualEntity;
import aprimorar.financeiro.cobrancas.domain.enums.StatusCobrancaIndividual;
import aprimorar.financeiro.cobrancas.repository.projections.CobrancaLoteProjection;
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

public interface CobrancaIndividualRepository
    extends JpaRepository<CobrancaIndividualEntity, Long>, JpaSpecificationExecutor<CobrancaIndividualEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CobrancaIndividualEntity c where c.id in :ids")
    List<CobrancaIndividualEntity> findAllByIdInForUpdate(@Param("ids") Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CobrancaIndividualEntity c where c.atendimentoId = :atendimentoId")
    Optional<CobrancaIndividualEntity> findByAtendimentoIdForUpdate(@Param("atendimentoId") Long atendimentoId);

    boolean existsByAtendimentoId(Long atendimentoId);

    boolean existsByAlunoIdAndStatus(UUID alunoId, StatusCobrancaIndividual status);

    List<CobrancaIndividualEntity> findAllByAtendimentoIdIn(Collection<Long> atendimentoIds);

    @Query(
        value = """
            select
                c.loteId as loteId,
                c.alunoId as alunoId,
                min(c.dataPagamento) as dataPagamento,
                c.formaPagamento as formaPagamento,
                c.comprovanteUrl as comprovanteUrl,
                sum(c.valor) as valorTotal,
                count(c.id) as quantidadeCobrancas
            from CobrancaIndividualEntity c
            where c.alunoId = :alunoId
              and c.status = :status
              and c.loteId is not null
            group by
                c.loteId,
                c.alunoId,
                c.formaPagamento,
                c.comprovanteUrl
            order by min(c.dataPagamento) desc
            """,
        countQuery = """
            select count(distinct c.loteId)
            from CobrancaIndividualEntity c
            where c.alunoId = :alunoId
              and c.status = :status
              and c.loteId is not null
            """
    )
    Page<CobrancaLoteProjection> findLotesCobrancasPorAlunoId(
        @Param("alunoId") UUID alunoId,
        @Param("status") StatusCobrancaIndividual status,
        Pageable pageable
    );

    @Query(
        """
            select
                c.loteId as loteId,
                c.alunoId as alunoId,
                min(c.dataPagamento) as dataPagamento,
                c.formaPagamento as formaPagamento,
                c.comprovanteUrl as comprovanteUrl,
                sum(c.valor) as valorTotal,
                count(c.id) as quantidadeCobrancas
            from CobrancaIndividualEntity c
            where c.loteId = :loteId
              and c.status = :status
            group by
                c.loteId,
                c.alunoId,
                c.formaPagamento,
                c.comprovanteUrl
            """
    )
    Optional<CobrancaLoteProjection> findLoteCobrancaPorId(
        @Param("loteId") UUID loteId,
        @Param("status") StatusCobrancaIndividual status
    );

}

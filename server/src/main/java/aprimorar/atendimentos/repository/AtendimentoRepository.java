package aprimorar.atendimentos.repository;


import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.enums.TipoAtendimentoEnum;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AtendimentoRepository extends JpaRepository<Atendimento, UUID>, JpaSpecificationExecutor<Atendimento> {
    interface TipoAtendimentoCount {
        TipoAtendimentoEnum getTipo();
        long getCount();
    }

    interface ReportAtendimentosProjection{
        long getTotalAulas();
        long getTotalMentoria();
        long getTotalTerapia();
        long getTotalOV();
        long getTotalENEM();
        long getTotalPAS();
        long getTotalOutros();
    }

    @Modifying
    @Query(
        """
        UPDATE Atendimento a
        SET a.alunoId = :ghostId
        WHERE a.alunoId = :alunoId
        """
    )
    void reassignAtendimentosAlunoToGhost(@Param("alunoId") UUID alunoId, @Param("ghostId") UUID ghostId);

    @Modifying
    @Query(
        """
        UPDATE Atendimento a
        SET a.colaboradorId = :ghostId
        WHERE a.colaboradorId = :colaboradorId
        """
    )
    void reassignAtendimentosColaboradorToGhost(@Param("colaboradorId") UUID colaboradorId, @Param("ghostId") UUID ghostId);

    @Query(
        value = """
        select
          a.colaboradorId as colaboradorId,
          count(a.id) as totalAtendimentos,
          coalesce(sum(a.repasse), 0) as totalPago,
          0 as totalPendente
        from Atendimento a
        where a.inicio >= coalesce(:inicio, a.inicio)
          and a.fim <= coalesce(:fim, a.fim)
        group by a.colaboradorId
        """,
        countQuery = """
        select count(distinct a.colaboradorId)
        from Atendimento a
        where a.inicio >= coalesce(:inicio, a.inicio)
          and a.fim <= coalesce(:fim, a.fim)
        """
    )

    ReportAtendimentosProjection getAtendimentosContentReport(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query(
        """
            select count(a) > 0
            from Atendimento a
            where a.alunoId = :alunoId
              and a.inicio < :fim
              and a.fim > :inicio
              and (:ignoredAtendimentoId is null or a.id <> :ignoredAtendimentoId)
        """
    )
    boolean alunoPossuiAtendimentoConflitante(
        @Param("alunoId") UUID alunoId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("ignoredAtendimentoId") UUID ignoredAtendimentoId
    );

    @Query(
        """
            select count(a) > 0
            from Atendimento a
            where a.colaboradorId = :colaboradorId
              and a.inicio < :fim
              and a.fim > :inicio
              and (:ignoredAtendimentoId is null or a.id <> :ignoredAtendimentoId)
        """
    )
    boolean colaboradorPossuiAtendimentoConflitante(
        @Param("colaboradorId") UUID colaboradorId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("ignoredAtendimentoId") UUID ignoredAtendimentoId
    );


}

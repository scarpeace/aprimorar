package aprimorar.atendimentos.repository;


import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.repository.projections.AtendimentoCalendarioProjection;
import aprimorar.atendimentos.repository.projections.AtendimentosReportProjection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>, JpaSpecificationExecutor<Atendimento> {

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

    @Query("""
        SELECT
          COUNT(*) FILTER (WHERE at.tipo = 'AULA') AS totalAulas,
          COUNT(*) FILTER (WHERE at.tipo = 'PAS') AS totalPAS,
          COUNT(*) FILTER (WHERE at.tipo = 'ENEM') AS totalENEM,
          COUNT(*) FILTER (WHERE at.tipo = 'MENTORIA') AS totalMentoria,
          COUNT(*) FILTER (WHERE at.tipo = 'TERAPIA') AS totalTerapia,
          COUNT(*) FILTER (WHERE at.tipo = 'ORIENTACAO_VOCACIONAL') AS totalOV,
          COUNT(*) FILTER (WHERE at.tipo = 'OUTRO') AS totalOutros
        FROM Atendimento at
        WHERE at.inicio >= :inicio
          AND at.fim <= :fim
        """)
    AtendimentosReportProjection getRelatorioMensal(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query("""
        SELECT
          at.id AS id,
          at.colaboradorId AS colaboradorId,
          at.alunoId AS alunoId,
          at.inicio AS inicio,
          at.fim AS fim,
          at.tipo AS tipo,
          c.nome AS nomeColaborador,
          al.nome AS nomeAluno
        FROM Atendimento at
        JOIN Colaborador c ON at.colaboradorId = c.id
        JOIN Aluno al ON at.alunoId = al.id
        WHERE at.inicio >= :inicio
          AND at.fim <= :fim
        ORDER BY at.inicio
        """)
    List<AtendimentoCalendarioProjection> getCalendarioMensal(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query(
        """
            SELECT count(a) > 0
            FROM Atendimento a
            WHERE a.alunoId = :alunoId
              AND a.inicio < :fim
              AND a.fim > :inicio
              AND (:ignoredAtendimentoId is null or a.id <> :ignoredAtendimentoId)
        """
    )
    boolean alunoPossuiAtendimentoConflitante(
        @Param("alunoId") UUID alunoId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("ignoredAtendimentoId") Long ignoredAtendimentoId
    );

    @Query(
        """
            SELECT count(a) > 0
            FROM Atendimento a
            WHERE a.colaboradorId = :colaboradorId
              AND a.inicio < :fim
              AND a.fim > :inicio
              AND (:ignoredAtendimentoId is null or a.id <> :ignoredAtendimentoId)
        """
    )
    boolean colaboradorPossuiAtendimentoConflitante(
        @Param("colaboradorId") UUID colaboradorId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("ignoredAtendimentoId") Long ignoredAtendimentoId
    );


}

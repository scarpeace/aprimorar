package aprimorar.atendimentos.repository;


import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.repository.projections.AtendimentoCalendarioProjection;
import aprimorar.atendimentos.repository.projections.AtendimentosReportProjection;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Colaborador;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>, JpaSpecificationExecutor<Atendimento> {

    @Override
    @EntityGraph(attributePaths = {"aluno", "colaborador"})
    Page<Atendimento> findAll(Specification<Atendimento> spec, Pageable pageable);

    @Modifying
    @Query(
        """
        UPDATE Atendimento a
        SET a.aluno = :ghost
        WHERE a.aluno.id = :alunoId
        """
    )
    void reassignAtendimentosAlunoToGhost(@Param("alunoId") UUID alunoId, @Param("ghost") Aluno ghost);

    @Modifying
    @Query(
        """
        UPDATE Atendimento a
        SET a.colaborador = :ghost
        WHERE a.colaborador.id = :colaboradorId
        """
    )
    void reassignAtendimentosColaboradorToGhost(@Param("colaboradorId") UUID colaboradorId, @Param("ghost") Colaborador ghost);

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
        WHERE at.dataHoraInicio >= :inicio
          AND at.dataHoraFim <= :fim
        """)
    AtendimentosReportProjection getRelatorioMensal(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query("""
        SELECT
          at.id AS id,
          at.colaborador.id AS colaboradorId,
          at.aluno.id AS alunoId,
          at.dataHoraInicio AS dataHoraInicio,
          at.dataHoraFim AS dataHoraFim,
          at.tipo AS tipo,
          c.nome AS nomeColaborador,
          al.nome AS nomeAluno
        FROM Atendimento at
        JOIN at.colaborador c
        JOIN at.aluno al
        WHERE at.dataHoraInicio >= :inicio
          AND at.dataHoraFim <= :fim
        ORDER BY at.dataHoraInicio
        """)
    List<AtendimentoCalendarioProjection> getCalendarioMensal(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query(
        """
            SELECT count(a) > 0
            FROM Atendimento a
            WHERE a.aluno.id = :alunoId
              AND a.dataHoraInicio < :fim
              AND a.dataHoraFim > :inicio
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
            WHERE a.colaborador.id = :colaboradorId
              AND a.dataHoraInicio < :fim
              AND a.dataHoraFim > :inicio
              AND (:ignoredAtendimentoId is null or a.id <> :ignoredAtendimentoId)
        """
    )
    boolean colaboradorPossuiAtendimentoConflitante(
        @Param("colaboradorId") UUID colaboradorId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("ignoredAtendimentoId") Long ignoredAtendimentoId
    );

    @Query(
        """
            SELECT count(a) > 0
            FROM Atendimento a
            WHERE a.aluno.id = :alunoId
              AND a.status = aprimorar.atendimentos.enums.StatusAtendimento.AGENDADO
        """
    )
    boolean alunoPossuiAtendimentoAgendado(@Param("alunoId") UUID alunoId);

    @Query(
        """
            SELECT count(a) > 0
            FROM Atendimento a
            WHERE a.aluno.id = :alunoId
              AND a.status = aprimorar.atendimentos.enums.StatusAtendimento.CONCLUIDO
              AND a.dataPagamentoAluno IS NULL
        """
    )
    boolean alunoPossuiPagamentoAlunoPendente(@Param("alunoId") UUID alunoId);

    @Query(
        """
            SELECT count(a) > 0
            FROM Atendimento a
            WHERE a.colaborador.id = :colaboradorId
              AND a.status = aprimorar.atendimentos.enums.StatusAtendimento.AGENDADO
        """
    )
    boolean colaboradorPossuiAtendimentoAgendado(@Param("colaboradorId") UUID colaboradorId);

    @Query(
        """
            SELECT count(a) > 0
            FROM Atendimento a
            WHERE a.colaborador.id = :colaboradorId
              AND a.status = aprimorar.atendimentos.enums.StatusAtendimento.CONCLUIDO
              AND a.dataPagamentoColaborador IS NULL
        """
    )
    boolean colaboradorPossuiRepassePendente(@Param("colaboradorId") UUID colaboradorId);


}

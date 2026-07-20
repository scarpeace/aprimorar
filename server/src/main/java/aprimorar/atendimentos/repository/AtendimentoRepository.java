package aprimorar.atendimentos.repository;


import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.dto.CalendarioAtendimentosResponse;
import aprimorar.atendimentos.repository.projections.AtendimentosReportProjection;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Colaborador;

import java.time.LocalDateTime;
import java.math.BigDecimal;
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
        SELECT new aprimorar.atendimentos.dto.CalendarioAtendimentosResponse(
          at.id,
          al.id,
          al.nome,
          c.id,
          c.nome,
          at.dataHoraInicio,
          at.dataHoraFim,
          at.tipo
        )
        FROM Atendimento at
        JOIN at.colaborador c
        JOIN at.aluno al
        WHERE at.dataHoraInicio >= :inicio
          AND at.dataHoraFim <= :fim
        ORDER BY at.dataHoraInicio
        """)
    List<CalendarioAtendimentosResponse> getCalendarioMensal(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query(
        """
            SELECT a
            FROM Atendimento a
            JOIN FETCH a.colaborador
            WHERE a.aluno.id = :alunoId
              AND a.status <> aprimorar.atendimentos.enums.StatusAtendimento.CANCELADO
              AND a.dataHoraInicio >= :inicio
              AND a.dataHoraFim <= :fim
            ORDER BY a.dataHoraInicio DESC
        """
    )
    List<Atendimento> findRelatorioAluno(
        @Param("alunoId") UUID alunoId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query(
        """
            SELECT new aprimorar.atendimentos.dto.ColaboradorResumoFinanceiroResponse(
              COUNT(a),
              COALESCE(SUM(a.repasseColaborador), 0),
              COALESCE(SUM(CASE WHEN a.dataRepasseColaborador IS NOT NULL THEN a.repasseColaborador ELSE 0 END), 0),
              COALESCE(SUM(CASE WHEN a.dataRepasseColaborador IS NULL THEN a.repasseColaborador ELSE 0 END), 0)
            )
            FROM Atendimento a
            WHERE a.colaborador.id = :colaboradorId
              AND a.status <> aprimorar.atendimentos.enums.StatusAtendimento.CANCELADO
              AND a.dataHoraInicio >= :inicio
              AND a.dataHoraFim <= :fim
        """
    )
    aprimorar.atendimentos.dto.ColaboradorResumoFinanceiroResponse getResumoFinanceiroColaborador(
        @Param("colaboradorId") UUID colaboradorId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query(
        """
            SELECT COALESCE(SUM(a.pagamentoAluno), 0)
            FROM Atendimento a
            WHERE a.aluno.id = :alunoId
              AND a.dataPagamentoAluno IS NOT NULL
              AND a.dataHoraInicio >= :inicio
              AND a.dataHoraFim <= :fim
        """
    )
    BigDecimal getTotalPagamentoPagoAluno(
        @Param("alunoId") UUID alunoId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query(
        """
            SELECT COALESCE(SUM(a.pagamentoAluno), 0)
            FROM Atendimento a
            WHERE a.aluno.id = :alunoId
              AND a.dataPagamentoAluno IS NULL
              AND a.dataHoraInicio >= :inicio
              AND a.dataHoraFim <= :fim
        """
    )
    BigDecimal getTotalPagamentoPendenteAluno(
        @Param("alunoId") UUID alunoId,
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
              AND a.dataRepasseColaborador IS NULL
        """
    )
    boolean colaboradorPossuiRepassePendente(@Param("colaboradorId") UUID colaboradorId);


}

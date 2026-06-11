package aprimorar.atendimentos.repository;


import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.enums.TipoAtendimentoEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    interface KpisAlunoProjection {
        UUID getAlunoId();
        String getAlunoNome();
        long getTotalAtendimentos();
        BigDecimal getTotalCobrado();
        BigDecimal getTotalPendente();
    }

    interface KpisColaboradorProjection {
        UUID getColaboradorId();
        String getColaboradorNome();
        long getTotalAtendimentos();
        BigDecimal getTotalPago();
        BigDecimal getTotalPendente();
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

    boolean existsByAlunoIdAndDataCobrancaAlunoIsNull(UUID alunoId);
    boolean existsByColaboradorIdAndDataPagamentoColaboradorIsNull(UUID colaboradorId);

    @Query(
        """
        SELECT COUNT(a)
        FROM Atendimento a
        WHERE a.inicio >= coalesce(:inicio, a.inicio)
        AND a.fim <= coalesce(:fim, a.fim)
        """
    )
    long countByPeriod(@Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Query(
        """
        SELECT COUNT(a)
        FROM Atendimento a
        WHERE a.alunoId = :alunoId
        AND a.inicio >= coalesce(:inicio, a.inicio)
        AND a.fim <= coalesce(:fim, a.fim)
        """
    )
    long countByAlunoIdInPeriod(@Param("alunoId") UUID alunoId, @Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Query(
        """
        SELECT COUNT(a)
        FROM Atendimento a
        WHERE a.colaboradorId = :colaboradorId
        AND a.inicio >= coalesce(:inicio, a.inicio)
        AND a.fim <= coalesce(:fim, a.fim)
        """
    )
    long countByColaboradorIdInPeriod(@Param("colaboradorId") UUID colaboradorId, @Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Query(
        """
        SELECT SUM(a.valor)
        FROM Atendimento a
        WHERE a.alunoId = :alunoId
        AND a.inicio >= coalesce(:inicio, a.inicio)
        AND a.fim <= coalesce(:fim, a.fim)
        AND a.dataCobrancaAluno IS NOT NULL
        """
    )
    BigDecimal sumCobradoByAlunoInPeriod(@Param("alunoId") UUID alunoId, @Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Query(
        """
        SELECT SUM(a.valor)
        FROM Atendimento a
        WHERE a.alunoId = :alunoId
        AND a.inicio >= coalesce(:inicio, a.inicio)
        AND a.fim <= coalesce(:fim, a.fim)
        AND a.dataCobrancaAluno IS NULL
        """
    )
    BigDecimal sumPendenteByAlunoInPeriod(@Param("alunoId") UUID alunoId, @Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Query("""
        SELECT COALESCE(SUM(a.repasse), 0)
        FROM Atendimento a
        WHERE a.colaboradorId = :colaboradorId
          AND a.inicio >= coalesce(:inicio, a.inicio)
          AND a.fim <= coalesce(:fim, a.fim)
          AND a.dataPagamentoColaborador IS NOT NULL
        """)
    BigDecimal sumPagoByColaboradorIdInPeriod(@Param("colaboradorId") UUID colaboradorId, @Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Query("""
        SELECT COALESCE(SUM(a.repasse), 0)
        FROM Atendimento a
        WHERE a.colaboradorId = :colaboradorId
          AND a.inicio >= coalesce(:inicio, a.inicio)
          AND a.fim <= coalesce(:fim, a.fim)
          AND a.dataPagamentoColaborador IS NULL
        """)
    BigDecimal sumPendenteByColaboradorIdInPeriod(@Param("colaboradorId") UUID colaboradorId, @Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Modifying
    @Query(
        """
        UPDATE Atendimento a
        SET a.alunoId = :ghostId,
            a.alunoNome = 'Aluno Removido'
        WHERE a.alunoId = :alunoId
        """
    )
    void reassignAtendimentosAlunoToGhost(@Param("alunoId") UUID alunoId, @Param("ghostId") UUID ghostId);

    @Modifying
    @Query(
        """
        UPDATE Atendimento a
        SET a.colaboradorId = :ghostId,
            a.colaboradorNome = 'Colaborador Removido'
        WHERE a.colaboradorId = :colaboradorId
        """
    )
    void reassignAtendimentosColaboradorToGhost(@Param("colaboradorId") UUID colaboradorId, @Param("ghostId") UUID ghostId);


    @Query(
        """
        select coalesce(sum(a.repasse), 0)
        from Atendimento a
        where a.dataPagamentoColaborador is not null
          and a.inicio >= coalesce(:inicio, a.inicio)
          and a.fim <= coalesce(:fim, a.fim)
        """
    )
    BigDecimal sumTotalPagoInPeriod(@Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Query(
        """
        select coalesce(sum(a.repasse), 0)
        from Atendimento a
        where a.dataPagamentoColaborador is null
          and a.inicio >= coalesce(:inicio, a.inicio)
          and a.fim <= coalesce(:fim, a.fim)
        """
    )
    BigDecimal sumTotalPendenteColaboradorInPeriod(@Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Query(
        """
        select coalesce(sum(a.valor), 0)
        from Atendimento a
        where a.dataCobrancaAluno is not null
          and a.inicio >= coalesce(:inicio, a.inicio)
          and a.fim <= coalesce(:fim, a.fim)
        """
    )
    BigDecimal sumTotalCobradoInPeriod(@Param("inicio") Instant inicio, @Param("fim") Instant fim);


    @Query(
        """
        select coalesce(sum(a.valor), 0)
        from Atendimento a
        where a.dataCobrancaAluno is null
          and a.inicio >= coalesce(:inicio, a.inicio)
          and a.fim <= coalesce(:fim, a.fim)
        """
    )
    BigDecimal sumTotalPendenteAlunoInPeriod(@Param("inicio") Instant inicio, @Param("fim") Instant fim);


    @Query(
        value = """
        select
          a.colaboradorId as colaboradorId,
          max(a.colaboradorNome) as colaboradorNome,
          count(a.id) as totalAtendimentos,
          coalesce(sum(case when a.dataPagamentoColaborador is not null then a.repasse else 0 end), 0) as totalPago,
          coalesce(sum(case when a.dataPagamentoColaborador is null then a.repasse else 0 end), 0) as totalPendente
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
    Page<KpisColaboradorProjection> getOverviewFinanceiroColaboradores(
        Pageable pageable,
        @Param("inicio") Instant inicio,
        @Param("fim") Instant fim
    );

    @Query(
        value = """
        select
          a.alunoId as alunoId,
          max(a.alunoNome) as alunoNome,
          count(a.id) as totalAtendimentos,
          coalesce(sum(case when a.dataCobrancaAluno is not null then a.valor else 0 end), 0) as totalCobrado,
          coalesce(sum(case when a.dataCobrancaAluno is null then a.valor else 0 end), 0) as totalPendente
        from Atendimento a
        where a.inicio >= coalesce(:inicio, a.inicio)
          and a.fim <= coalesce(:fim, a.fim)
        group by a.alunoId
        """,
        countQuery = """
        select count(distinct a.alunoId)
        from Atendimento a
        where a.inicio >= coalesce(:inicio, a.inicio)
          and a.fim <= coalesce(:fim, a.fim)
        """
    )
    Page<KpisAlunoProjection> getOverviewFinanceiroAlunos(
        Pageable pageable,
        @Param("inicio") Instant inicio,
        @Param("fim") Instant fim
    );

    @Query(
        """
            select
                count(case when a.tipo = 'AULA' then 1 end) as totalAulas,
                count(case when a.tipo = 'MENTORIA' then 1 end) as totalMentoria,
                count(case when a.tipo = 'TERAPIA' then 1 end) as totalTerapia,
                count(case when a.tipo = 'OV' then 1 end) as totalOV,
                count(case when a.tipo = 'ENEM' then 1 end) as totalENEM,
                count(case when a.tipo = 'PAS' then 1 end) as totalPAS,
                count(case when a.tipo = 'OUTROS' then 1 end) as totalOutros
            from Atendimento a
            where a.inicio >= coalesce(:inicio, a.inicio)
              and a.fim <= coalesce(:fim, a.fim)
        """
    )
    ReportAtendimentosProjection getAtendimentosContentReport(
        @Param("inicio") Instant inicio,
        @Param("fim") Instant fim
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
        @Param("inicio") Instant inicio,
        @Param("fim") Instant fim,
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
        @Param("inicio") Instant inicio,
        @Param("fim") Instant fim,
        @Param("ignoredAtendimentoId") UUID ignoredAtendimentoId
    );

    @Query(
        """
        select count(distinct a.alunoId)
        from Atendimento a
        where a.inicio >= :inicio
          and a.inicio < :fim
        """
    )
    long countAlunosInPeriod(
        @Param("inicio") Instant inicio,
        @Param("fim") Instant fim
    );

    @Query(
        value = """
        select count(distinct a.aluno_id)
        from atendimentos a
        join alunos s on s.id = a.aluno_id
        where a.inicio >= :inicio
          and a.inicio < :fim
          and s.ativo = true
        """,
        nativeQuery = true
    )
    long countAlunosAtivosInPeriod(
        @Param("inicio") Instant inicio,
        @Param("fim") Instant fim
    );


    @Query(
        """
        select count(a)
        from Atendimento a
        where a.inicio >= :inicio
          and a.inicio < :fim
        """
    )
    long countAtendimentosInPeriod(
        @Param("inicio") Instant inicio,
        @Param("fim") Instant fim
    );


}

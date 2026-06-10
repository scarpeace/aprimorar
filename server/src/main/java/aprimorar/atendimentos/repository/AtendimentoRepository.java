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
        TipoAtendimentoEnum getContent();
        long getCount();
    }

    interface KpisAlunoProjection {
        UUID getStudentId();
        String getStudentName();
        long getTotalAtendimentos();
        BigDecimal getTotalCharged();
        BigDecimal getTotalPending();
    }

    interface KpisColaboradorProjection {
        UUID getEmployeeId();
        String getEmployeeName();
        long getTotalAtendimentos();
        BigDecimal getTotalPaid();
        BigDecimal getTotalPending();
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

    boolean existsByStudentIdAndStudentChargeDateIsNull(UUID studentId);
    boolean existsByEmployeeIdAndEmployeePaymentDateIsNull(UUID employeeId);

    @Query(
        """
        SELECT COUNT(a)
        FROM Atendimento a
        WHERE a.startDate >= coalesce(:startDate, a.startDate)
        AND a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    long countByPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        SELECT COUNT(a)
        FROM Atendimento a
        WHERE a.studentId = :studentId
        AND a.startDate >= coalesce(:startDate, a.startDate)
        AND a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    long countByStudentIdInPeriod(@Param("studentId") UUID studentId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        SELECT COUNT(a)
        FROM Atendimento a
        WHERE a.employeeId = :employeeId
        AND a.startDate >= coalesce(:startDate, a.startDate)
        AND a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    long countByEmployeeIdInPeriod(@Param("employeeId") UUID employeeId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        SELECT SUM(a.price)
        FROM Atendimento a
        WHERE a.studentId = :studentId
        AND a.startDate >= coalesce(:startDate, a.startDate)
        AND a.endDate <= coalesce(:endDate, a.endDate)
        AND a.studentChargeDate IS NOT NULL
        """
    )
    BigDecimal sumChargedByStudentInPeriod(@Param("studentId") UUID studentId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        SELECT SUM(a.price)
        FROM Atendimento a
        WHERE a.studentId = :studentId
        AND a.startDate >= coalesce(:startDate, a.startDate)
        AND a.endDate <= coalesce(:endDate, a.endDate)
        AND a.studentChargeDate IS NULL
        """
    )
    BigDecimal sumPendingByStudentInPeriod(@Param("studentId") UUID studentId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("""
        SELECT COALESCE(SUM(a.payment), 0)
        FROM Atendimento a
        WHERE a.employeeId = :employeeId
          AND a.startDate >= coalesce(:startDate, a.startDate)
          AND a.endDate <= coalesce(:endDate, a.endDate)
          AND a.employeePaymentDate IS NOT NULL
        """)
    BigDecimal sumPaidByEmployeeIdInPeriod(@Param("employeeId") UUID employeeId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("""
        SELECT COALESCE(SUM(a.payment), 0)
        FROM Atendimento a
        WHERE a.employeeId = :employeeId
          AND a.startDate >= coalesce(:startDate, a.startDate)
          AND a.endDate <= coalesce(:endDate, a.endDate)
          AND a.employeePaymentDate IS NULL
        """)
    BigDecimal sumUnpaidByEmployeeIdInPeriod(@Param("employeeId") UUID employeeId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Modifying
    @Query(
        """
        UPDATE Atendimento a
        SET a.studentId = :ghostId,
            a.studentName = 'Aluno Removido'
        WHERE a.studentId = :studentId
        """
    )
    void reassingAtendimentoAlunosToGhost(@Param("studentId") UUID studentId, @Param("ghostId") UUID ghostId);

    @Modifying
    @Query(
        """
        UPDATE Atendimento a
        SET a.employeeId = :ghostId,
            a.employeeName = 'Colaborador Removido'
        WHERE a.employeeId = :employeeId
        """
    )
    void reassignAtendimentosColaboradorToGhost(@Param("employeeId") UUID employeeId, @Param("ghostId") UUID ghostId);


    @Query(
        """
        select coalesce(sum(a.payment), 0)
        from Atendimento a
        where a.employeePaymentDate is not null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumTotalPaidInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        select coalesce(sum(a.payment), 0)
        from Atendimento a
        where a.employeePaymentDate is null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumTotalUnpaidInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        select coalesce(sum(a.price), 0)
        from Atendimento a
        where a.studentChargeDate is not null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumTotalChargedInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);


    @Query(
        """
        select coalesce(sum(a.price), 0)
        from Atendimento a
        where a.studentChargeDate is null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumTotalUnchargedInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);


    @Query(
        value = """
        select
          a.employeeId as employeeId,
          max(a.employeeName) as employeeName,
          count(a.id) as totalAtendimentos,
          coalesce(sum(case when a.employeePaymentDate is not null then a.payment else 0 end), 0) as totalPaid,
          coalesce(sum(case when a.employeePaymentDate is null then a.payment else 0 end), 0) as totalPending
        from Atendimento a
        where a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        group by a.employeeId
        """,
        countQuery = """
        select count(distinct a.employeeId)
        from Atendimento a
        where a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    Page<KpisColaboradorProjection> getOverviewFinanceiroColaboradores(
        Pageable pageable,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query(
        value = """
        select
          a.studentId as studentId,
          max(a.studentName) as studentName,
          count(a.id) as totalAtendimentos,
          coalesce(sum(case when a.studentChargeDate is not null then a.price else 0 end), 0) as totalCharged,
          coalesce(sum(case when a.studentChargeDate is null then a.price else 0 end), 0) as totalPending
        from Atendimento a
        where a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        group by a.studentId
        """,
        countQuery = """
        select count(distinct a.studentId)
        from Atendimento a
        where a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    Page<KpisAlunoProjection> getOverviewFinanceiroAlunos(
        Pageable pageable,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query(
        """
            select
                count(case when a.content = 'AULA' then 1 end) as totalAulas,
                count(case when a.content = 'MENTORIA' then 1 end) as totalMentoria,
                count(case when a.content = 'TERAPIA' then 1 end) as totalTerapia,
                count(case when a.content = 'OV' then 1 end) as totalOV,
                count(case when a.content = 'ENEM' then 1 end) as totalENEM,
                count(case when a.content = 'PAS' then 1 end) as totalPAS,
                count(case when a.content = 'OUTROS' then 1 end) as totalOutros
            from Atendimento a
            where a.startDate >= coalesce(:startDate, a.startDate)
              and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    ReportAtendimentosProjection getAtendimentosContentReport(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query(
        """
            select count(a) > 0
            from Atendimento a
            where a.studentId = :studentId
              and a.startDate < :endDate
              and a.endDate > :startDate
              and (:ignoredAtendimentoId is null or a.id <> :ignoredAtendimentoId)
        """
    )
    boolean studentHasConflictingAtendimento(
        @Param("studentId") UUID studentId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("ignoredAtendimentoId") UUID ignoredAtendimentoId
    );

    @Query(
        """
            select count(a) > 0
            from Atendimento a
            where a.employeeId = :employeeId
              and a.startDate < :endDate
              and a.endDate > :startDate
              and (:ignoredAtendimentoId is null or a.id <> :ignoredAtendimentoId)
        """
    )
    boolean employeeHasConflictingAtendimento(
        @Param("employeeId") UUID employeeId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("ignoredAtendimentoId") UUID ignoredAtendimentoId
    );

    @Query(
        """
        select count(distinct a.studentId)
        from Atendimento a
        where a.startDate >= :startDate
          and a.startDate < :endDate
        """
    )
    long countStudentsInPeriod(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query(
        value = """
        select count(distinct a.student_id)
        from tb_appointments a
        join tb_students s on s.id = a.student_id
        where a.start_date >= :startDate
          and a.start_date < :endDate
          and s.active = true
        """,
        nativeQuery = true
    )
    long countActiveStudentsInPeriod(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );


    @Query(
        """
        select count(a)
        from Atendimento a
        where a.startDate >= :startDate
          and a.startDate < :endDate
        """
    )
    long countAtendimentosInPeriod(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );


}

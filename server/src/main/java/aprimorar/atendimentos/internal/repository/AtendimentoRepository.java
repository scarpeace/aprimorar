package aprimorar.atendimentos.internal.repository;

import aprimorar.atendimentos.internal.Atendimento;
import aprimorar.atendimentos.internal.TipoAtendimentoEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
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

    interface AlunoFinanceSummaryProjection {
        UUID getStudentId();
        String getStudentName();
        long getTotalAtendimentos();
        BigDecimal getTotalCharged();
        BigDecimal getTotalPending();
    }

    interface ColaboradorFinanceSummaryProjection {
        UUID getEmployeeId();
        String getEmployeeName();
        long getTotalAtendimentos();
        BigDecimal getTotalPaid();
        BigDecimal getTotalPending();
    }

    @Modifying
    @Query(
        """
        UPDATE Atendimento a
        SET a.studentId = :ghostId,
            a.studentName = 'Aluno Removido'
        WHERE a.studentId = :studentId
        """
    )
    void reassignStudentAtendimentosToGhost(@Param("studentId") UUID studentId, @Param("ghostId") UUID ghostId);

    @Modifying
    @Query(
        """
        UPDATE Atendimento a
        SET a.employeeId = :ghostId,
            a.employeeName = 'Colaborador Removido'
        WHERE a.employeeId = :employeeId
        """
    )
    void reassignEmployeeAtendimentosToGhost(@Param("employeeId") UUID employeeId, @Param("ghostId") UUID ghostId);

    boolean existsByStudentIdAndStudentChargeDateIsNull(UUID studentId);

    boolean existsByEmployeeIdAndEmployeePaymentDateIsNull(UUID employeeId);

    @Query(
        """
        select coalesce(sum(a.payment), 0)
        from Atendimento a
        where a.employeePaymentDate is not null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumPaidFiltered(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        select coalesce(sum(a.payment), 0)
        from Atendimento a
        where a.employeePaymentDate is null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumUnpaidFiltered(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(a.payment), 0) FROM Atendimento a WHERE a.employeeId = :employeeId AND a.startDate BETWEEN :startDate AND :endDate AND a.employeePaymentDate IS NOT NULL")
    BigDecimal sumPaidByEmployeeIdInPeriod(@Param("employeeId") UUID employeeId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(a.payment), 0) FROM Atendimento a WHERE a.employeeId = :employeeId AND a.startDate BETWEEN :startDate AND :endDate AND a.employeePaymentDate IS NULL")
    BigDecimal sumUnpaidByEmployeeIdInPeriod(@Param("employeeId") UUID employeeId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(a.payment), 0) FROM Atendimento a WHERE a.employeeId = :employeeId AND a.employeePaymentDate IS NOT NULL")
    BigDecimal sumPaidByEmployeeId(@Param("employeeId") UUID employeeId);

    @Query("SELECT COALESCE(SUM(a.payment), 0) FROM Atendimento a WHERE a.employeeId = :employeeId AND a.employeePaymentDate IS NULL")
    BigDecimal sumUnpaidByEmployeeId(@Param("employeeId") UUID employeeId);

    @Query(
        """
        select count(a)
        from Atendimento a
        where a.employeeId = :employeeId
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    long countFilteredByEmployeeId(
        @Param("employeeId") UUID employeeId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query(
        """
        select coalesce(sum(a.payment), 0)
        from Atendimento a
        where a.employeeId = :employeeId
          and a.employeePaymentDate is not null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumPaidFilteredByEmployeeId(
        @Param("employeeId") UUID employeeId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query(
        """
        select coalesce(sum(a.payment), 0)
        from Atendimento a
        where a.employeeId = :employeeId
          and a.employeePaymentDate is null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumUnpaidFilteredByEmployeeId(
        @Param("employeeId") UUID employeeId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query(
        """
        select coalesce(sum(a.price), 0)
        from Atendimento a
        where a.studentChargeDate is not null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumChargedFiltered(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        select coalesce(sum(a.price), 0)
        from Atendimento a
        where a.studentChargeDate is null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumPendingFiltered(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Atendimento a WHERE a.studentId = :studentId AND a.startDate BETWEEN :startDate AND :endDate AND a.studentChargeDate IS NOT NULL")
    BigDecimal sumChargedByStudentIdInPeriod(@Param("studentId") UUID studentId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Atendimento a WHERE a.studentId = :studentId AND a.startDate BETWEEN :startDate AND :endDate AND a.studentChargeDate IS NULL")
    BigDecimal sumPendingByStudentIdInPeriod(@Param("studentId") UUID studentId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Atendimento a WHERE a.studentId = :studentId AND a.studentChargeDate IS NOT NULL")
    BigDecimal sumChargedByStudentId(@Param("studentId") UUID studentId);

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Atendimento a WHERE a.studentId = :studentId AND a.studentChargeDate IS NULL")
    BigDecimal sumPendingByStudentId(@Param("studentId") UUID studentId);

    @Query(
        """
        select count(a)
        from Atendimento a
        where a.studentId = :studentId
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    long countFilteredByStudentId(
        @Param("studentId") UUID studentId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query(
        """
        select coalesce(sum(a.price), 0)
        from Atendimento a
        where a.studentId = :studentId
          and a.studentChargeDate is not null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumChargedFilteredByStudentId(
        @Param("studentId") UUID studentId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query(
        """
        select coalesce(sum(a.price), 0)
        from Atendimento a
        where a.studentId = :studentId
          and a.studentChargeDate is null
          and a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        """
    )
    BigDecimal sumPendingFilteredByStudentId(
        @Param("studentId") UUID studentId,
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
          and a.studentId <> :excludedStudentId
        """
    )
    long countDistinctStudentsInPeriodExcludingAluno(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("excludedStudentId") UUID excludedStudentId
    );

    @Query(
        """
        select a.content as content, count(a) as count
        from Atendimento a
        where a.startDate >= :startDate
          and a.startDate < :endDate
        group by a.content
        order by count(a) desc
        """
    )
    List<TipoAtendimentoCount> findContentDistributionInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        select
          a.studentId as studentId,
          a.studentName as studentName,
          count(a) as totalAtendimentos,
          coalesce(sum(case when a.studentChargeDate is not null then a.price else 0 end), 0) as totalCharged,
          coalesce(sum(case when a.studentChargeDate is null then a.price else 0 end), 0) as totalPending
        from Atendimento a
        where a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        group by a.studentId, a.studentName
        order by a.studentName asc
        """
    )
    List<AlunoFinanceSummaryProjection> findAlunoFinanceSummaries(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query(
        """
        select
          a.employeeId as employeeId,
          a.employeeName as employeeName,
          count(a) as totalAtendimentos,
          coalesce(sum(case when a.employeePaymentDate is not null then a.payment else 0 end), 0) as totalPaid,
          coalesce(sum(case when a.employeePaymentDate is null then a.payment else 0 end), 0) as totalPending
        from Atendimento a
        where a.startDate >= coalesce(:startDate, a.startDate)
          and a.endDate <= coalesce(:endDate, a.endDate)
        group by a.employeeId, a.employeeName
        order by a.employeeName asc
        """
    )
    List<ColaboradorFinanceSummaryProjection> findColaboradorFinanceSummaries(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    long countByStartDateGreaterThanEqualAndStartDateLessThan(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

}

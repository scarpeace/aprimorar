package aprimorar.appointment.internal.repository;

import aprimorar.appointment.internal.Appointment;
import aprimorar.appointment.api.AppointmentContent;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID>, JpaSpecificationExecutor<Appointment> {
    interface AppointmentContentCount {
        AppointmentContent getContent();
        long getCount();
    }

    Page<Appointment> findAllByEmployeeId(UUID employeeId, Pageable pageable);

    Page<Appointment> findAllByStudentId(UUID studentId, Pageable pageable);


    @Modifying
    @Query("UPDATE Appointment a SET a.studentId = :ghostId WHERE a.studentId = :studentId")
    void reassignStudentAppointmentsToGhost(@Param("studentId") UUID studentId, @Param("ghostId") UUID ghostId);

    @Modifying
    @Query("UPDATE Appointment a SET a.employeeId = :ghostId WHERE a.employeeId = :employeeId")
    void reassignEmployeeAppointmentsToGhost(@Param("employeeId") UUID employeeId, @Param("ghostId") UUID ghostId);

    long countByEmployeeIdAndStartDateBetween(UUID employeeId, Instant startDate, Instant endDate);

    long countByEmployeeId(UUID employeeId);

    @Query("SELECT COALESCE(SUM(a.payment), 0) FROM Appointment a WHERE a.employeePaymentDate IS NOT NULL")
    BigDecimal sumTotalEmployeePayment();

    @Query("SELECT COALESCE(SUM(a.payment), 0) FROM Appointment a WHERE a.employeePaymentDate IS NULL")
    BigDecimal sumTotalEmployeePaymentPending();

    @Query("SELECT COALESCE(SUM(a.payment), 0) FROM Appointment a WHERE a.employeeId = :employeeId AND a.startDate BETWEEN :startDate AND :endDate AND a.employeePaymentDate IS NOT NULL")
    BigDecimal sumPaidByEmployeeIdInPeriod(@Param("employeeId") UUID employeeId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(a.payment), 0) FROM Appointment a WHERE a.employeeId = :employeeId AND a.startDate BETWEEN :startDate AND :endDate AND a.employeePaymentDate IS NULL")
    BigDecimal sumUnpaidByEmployeeIdInPeriod(@Param("employeeId") UUID employeeId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(a.payment), 0) FROM Appointment a WHERE a.employeeId = :employeeId AND a.employeePaymentDate IS NOT NULL")
    BigDecimal sumPaidByEmployeeId(@Param("employeeId") UUID employeeId);

    @Query("SELECT COALESCE(SUM(a.payment), 0) FROM Appointment a WHERE a.employeeId = :employeeId AND a.employeePaymentDate IS NULL")
    BigDecimal sumUnpaidByEmployeeId(@Param("employeeId") UUID employeeId);

    @Query(
        """
        select count(a)
        from Appointment a
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
        from Appointment a
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
        from Appointment a
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

    long countByStudentIdAndStartDateBetween(UUID studentId, Instant startDate, Instant endDate);

    long countByStudentId(UUID studentId);

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Appointment a WHERE a.studentChargeDate IS NOT NULL")
    BigDecimal sumTotalStudentIncome();

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Appointment a WHERE a.studentChargeDate IS NULL")
    BigDecimal sumTotalStudentIncomePending();

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Appointment a WHERE a.studentId = :studentId AND a.startDate BETWEEN :startDate AND :endDate AND a.studentChargeDate IS NOT NULL")
    BigDecimal sumChargedByStudentIdInPeriod(@Param("studentId") UUID studentId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Appointment a WHERE a.studentId = :studentId AND a.startDate BETWEEN :startDate AND :endDate AND a.studentChargeDate IS NULL")
    BigDecimal sumPendingByStudentIdInPeriod(@Param("studentId") UUID studentId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Appointment a WHERE a.studentId = :studentId AND a.studentChargeDate IS NOT NULL")
    BigDecimal sumChargedByStudentId(@Param("studentId") UUID studentId);

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Appointment a WHERE a.studentId = :studentId AND a.studentChargeDate IS NULL")
    BigDecimal sumPendingByStudentId(@Param("studentId") UUID studentId);

    @Query(
        """
        select count(a)
        from Appointment a
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
        from Appointment a
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
        from Appointment a
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
            from Appointment a
            where a.studentId = :studentId
              and a.startDate < :endDate
              and a.endDate > :startDate
              and (:ignoredAppointmentId is null or a.id <> :ignoredAppointmentId)
        """
    )
    boolean studentHasConflictingAppointment(
        @Param("studentId") UUID studentId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("ignoredAppointmentId") UUID ignoredAppointmentId
    );

    @Query(
        """
            select count(a) > 0
            from Appointment a
            where a.employeeId = :employeeId
              and a.startDate < :endDate
              and a.endDate > :startDate
              and (:ignoredAppointmentId is null or a.id <> :ignoredAppointmentId)
        """
    )
    boolean employeeHasConflictingAppointment(
        @Param("employeeId") UUID employeeId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("ignoredAppointmentId") UUID ignoredAppointmentId
    );

    @Query(
        """
        select count(distinct a.studentId)
        from Appointment a
        where a.startDate >= :startDate
          and a.startDate < :endDate
          and a.studentId <> :excludedStudentId
        """
    )
    long countDistinctStudentsInPeriodExcludingStudent(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("excludedStudentId") UUID excludedStudentId
    );

    @Query(
        """
        select coalesce(sum(a.price), 0)
        from Appointment a
        where a.startDate >= :startDate
          and a.startDate < :endDate
        """
    )
    BigDecimal sumPriceInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        select coalesce(sum(a.payment), 0)
        from Appointment a
        where a.startDate >= :startDate
          and a.startDate < :endDate
        """
    )
    BigDecimal sumPaymentInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        select a.content as content, count(a) as count
        from Appointment a
        where a.startDate >= :startDate
          and a.startDate < :endDate
        group by a.content
        order by count(a) desc
        """
    )
    List<AppointmentContentCount> findContentDistributionInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    long countByStartDateGreaterThanEqualAndStartDateLessThan(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

}

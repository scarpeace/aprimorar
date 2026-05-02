package com.aprimorar.api.domain.event.repository;

import com.aprimorar.api.domain.event.Event;
import com.aprimorar.api.enums.EventContent;
import java.math.BigDecimal;
import java.time.Instant;
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

public interface EventRepository extends JpaRepository<Event, UUID>, JpaSpecificationExecutor<Event> {
    interface EventContentCount {
        EventContent getContent();
        long getCount();
    }

    @EntityGraph(attributePaths = { "student", "employee" })
    Page<Event> findAll(Specification<Event> spec, Pageable pageable);

    @EntityGraph(attributePaths = { "student", "employee" })
    Page<Event> findAll(Pageable pageable);

    @EntityGraph(attributePaths = { "student", "employee" })
    java.util.Optional<Event> findById(UUID id);

    @EntityGraph(attributePaths = { "student", "employee" })
    Page<Event> findAllByEmployeeId(UUID employeeId, Pageable pageable);

    @EntityGraph(attributePaths = { "student", "employee" })
    Page<Event> findAllByStudentId(UUID studentId, Pageable pageable);


    @Modifying
    @Query("UPDATE Event e SET e.student.id = :ghostId WHERE e.student.id = :studentId")
    void reassignStudentEventsToGhost(@Param("studentId") UUID studentId, @Param("ghostId") UUID ghostId);

    @Modifying
    @Query("UPDATE Event e SET e.employee.id = :ghostId WHERE e.employee.id = :employeeId")
    void reassignEmployeeEventsToGhost(@Param("employeeId") UUID employeeId, @Param("ghostId") UUID ghostId);

    long countByEmployeeIdAndStartDateBetween(UUID employeeId, Instant startDate, Instant endDate);

    @Query("SELECT COALESCE(SUM(e.payment), 0) FROM Event e WHERE e.employeePaymentDate IS NOT NULL")
    BigDecimal sumTotalEmployeePayment();

    @Query("SELECT COALESCE(SUM(e.payment), 0) FROM Event e WHERE e.employeePaymentDate IS NULL")
    BigDecimal sumTotalEmployeePaymentPending();

    @Query("SELECT COALESCE(SUM(e.payment), 0) FROM Event e WHERE e.employee.id = :employeeId AND e.startDate BETWEEN :startDate AND :endDate AND e.employeePaymentDate IS NOT NULL")
    BigDecimal sumPaidByEmployeeIdInPeriod(@Param("employeeId") UUID employeeId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(e.payment), 0) FROM Event e WHERE e.employee.id = :employeeId AND e.startDate BETWEEN :startDate AND :endDate AND e.employeePaymentDate IS NULL")
    BigDecimal sumUnpaidByEmployeeIdInPeriod(@Param("employeeId") UUID employeeId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    long countByStudentIdAndStartDateBetween(UUID studentId, Instant startDate, Instant endDate);

    @Query("SELECT COALESCE(SUM(e.price), 0) FROM Event e WHERE e.studentChargeDate IS NOT NULL")
    BigDecimal sumTotalStudentIncome();

    @Query("SELECT COALESCE(SUM(e.price), 0) FROM Event e WHERE e.studentChargeDate IS NULL")
    BigDecimal sumTotalStudentIncomePending();

    @Query("SELECT COALESCE(SUM(e.price), 0) FROM Event e WHERE e.student.id = :studentId AND e.startDate BETWEEN :startDate AND :endDate AND e.studentChargeDate IS NOT NULL")
    BigDecimal sumChargedByStudentIdInPeriod(@Param("studentId") UUID studentId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(e.price), 0) FROM Event e WHERE e.student.id = :studentId AND e.startDate BETWEEN :startDate AND :endDate AND e.studentChargeDate IS NULL")
    BigDecimal sumPendingByStudentIdInPeriod(@Param("studentId") UUID studentId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
            select count(e) > 0
            from Event e
            where e.student.id = :studentId
              and e.startDate < :endDate
              and e.endDate > :startDate
              and (:ignoredEventId is null or e.id <> :ignoredEventId)
        """
    )
    boolean studentHasConflictingEvent(
        @Param("studentId") UUID studentId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("ignoredEventId") UUID ignoredEventId
    );

    @Query(
        """
            select count(e) > 0
            from Event e
            where e.employee.id = :employeeId
              and e.startDate < :endDate
              and e.endDate > :startDate
              and (:ignoredEventId is null or e.id <> :ignoredEventId)
        """
    )
    boolean employeeHasConflictingEvent(
        @Param("employeeId") UUID employeeId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("ignoredEventId") UUID ignoredEventId
    );

    @Query(
        """
        select count(distinct e.student.id)
        from Event e
        where e.startDate >= :startDate
          and e.startDate < :endDate
          and e.student.id <> :excludedStudentId
        """
    )
    long countDistinctStudentsInPeriodExcludingStudent(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("excludedStudentId") UUID excludedStudentId
    );

    @Query(
        """
        select coalesce(sum(e.price), 0)
        from Event e
        where e.startDate >= :startDate
          and e.startDate < :endDate
        """
    )
    BigDecimal sumPriceInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        select coalesce(sum(e.payment), 0)
        from Event e
        where e.startDate >= :startDate
          and e.startDate < :endDate
        """
    )
    BigDecimal sumPaymentInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        """
        select e.content as content, count(e) as count
        from Event e
        where e.startDate >= :startDate
          and e.startDate < :endDate
        group by e.content
        order by count(e) desc
        """
    )
    List<EventContentCount> findContentDistributionInPeriod(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    long countByStartDateGreaterThanEqualAndStartDateLessThan(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
}

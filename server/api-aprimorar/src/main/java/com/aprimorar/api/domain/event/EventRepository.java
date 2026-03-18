package com.aprimorar.api.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventRepository extends JpaRepository<Event, UUID>, JpaSpecificationExecutor<Event> {

    @Modifying
    @Query("UPDATE Event e SET e.student.id = :ghostId WHERE e.student.id = :studentId")
    void reassignEventsToGhost(@Param("studentId") UUID studentId, @Param("ghostId") UUID ghostId);

    @Modifying
    @Query("UPDATE Event e SET e.employee.id = :ghostId WHERE e.employee.id = :employeeId")
    void reassignEmployeeEventsToGhost(@Param("employeeId") UUID employeeId, @Param("ghostId") UUID ghostId);

    @Override
    @EntityGraph(attributePaths = {"student", "employee"})
    Page<Event> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"student", "employee"})
    Page<Event> findAllByEmployeeId(UUID employeeId, Pageable pageable);

    @EntityGraph(attributePaths = {"student", "employee"})
    Page<Event> findAllByStudentId(UUID studentId, Pageable pageable);

    @EntityGraph(attributePaths = {"student", "employee"})
    @Query("""
            SELECT e
            FROM Event e
            WHERE e.startDate >= COALESCE(:start, e.startDate)
              AND e.startDate <= COALESCE(:end, e.startDate)
              AND e.student.id = COALESCE(:studentId, e.student.id)
              AND e.employee.id = COALESCE(:employeeId, e.employee.id)
            """)
    Page<Event> findAllWithFilter(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("studentId") UUID studentId,
            @Param("employeeId") UUID employeeId,
            Pageable pageable
    );

    @Query("""
                select count(e) > 0
                from Event e
                where e.student.id = :studentId
                  and e.startDate < :endDate
                  and e.endDate > :startDate
                  and (:ignoredEventId is null or e.id <> :ignoredEventId)
            """)
    boolean studentHasConflictingEvent(
            @Param("studentId") UUID studentId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("ignoredEventId") UUID ignoredEventId
    );

    @Query("""
                select count(e) > 0
                from Event e
                where e.employee.id = :employeeId
                  and e.startDate < :endDate
                  and e.endDate > :startDate
                  and (:ignoredEventId is null or e.id <> :ignoredEventId)
            """)
    boolean employeeHasConflictingEvent(
            @Param("employeeId") UUID employeeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("ignoredEventId") UUID ignoredEventId
    );


}

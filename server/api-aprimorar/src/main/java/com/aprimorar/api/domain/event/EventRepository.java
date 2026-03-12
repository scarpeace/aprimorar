package com.aprimorar.api.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Override
    @EntityGraph(attributePaths = {"studentEntity", "employee"})
    Page<Event> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"studentEntity", "employee"})
    Page<Event> findAllByEmployeeEntityId(UUID employeeId, Pageable pageable);

    @EntityGraph(attributePaths = {"studentEntity", "employee"})
    Page<Event> findAllByStudentEntityId(UUID studentId, Pageable pageable);

    @EntityGraph(attributePaths = {"studentEntity", "employee"})
    @Query("""
            SELECT e
            FROM EventEntity e
            WHERE e.startDateTime >= COALESCE(:start, e.startDateTime)
              AND e.startDateTime <= COALESCE(:end, e.startDateTime)
              AND e.studentEntity.id = COALESCE(:studentId, e.studentEntity.id)
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
                  and e.startDateTime < :endDateTime
                  and e.endDateTime > :startDateTime
            """)
    boolean studentHasConflictingEvent(
            @Param("studentId") UUID studentId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query("""
                select count(e) > 0
                from EventEntity e
                where e.employee.id = :employeeId
                  and e.startDateTime < :endDateTime
                  and e.endDateTime > :startDateTime
            """)
    boolean employeeHasConflictingEvent(
            @Param("employeeId") UUID employeeId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );


}

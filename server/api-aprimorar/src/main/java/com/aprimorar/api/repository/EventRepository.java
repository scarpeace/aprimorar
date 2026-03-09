package com.aprimorar.api.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aprimorar.api.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(attributePaths = {"student", "employee"})
    @Query("""
            SELECT e
            FROM Event e
            WHERE e.startDateTime >= COALESCE(:start, e.startDateTime)
              AND e.startDateTime <= COALESCE(:end, e.startDateTime)
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
}

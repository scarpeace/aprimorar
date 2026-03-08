package com.aprimorar.api.repository;

import com.aprimorar.api.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(attributePaths = {"student", "employee"})
    @Query("""
            SELECT e
            FROM Event e
            WHERE (:start IS NULL OR e.startDateTime >= :start)
              AND (:end IS NULL OR e.startDateTime <= :end)
              AND (:studentId IS NULL OR e.student.id = :studentId)
              AND (:employeeId IS NULL OR e.employee.id = :employeeId)
            """)
    Page<Event> findAllWithFilter(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("studentId") UUID studentId,
            @Param("employeeId") UUID employeeId,
            Pageable pageable
    );
}

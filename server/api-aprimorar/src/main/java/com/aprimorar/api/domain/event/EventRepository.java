package com.aprimorar.api.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @EntityGraph(attributePaths = {"studentEntity", "employeeEntity"})
    @Query("""
            SELECT e
            FROM Event e
            WHERE e.startDateTime >= COALESCE(:start, e.startDateTime)
              AND e.startDateTime <= COALESCE(:end, e.startDateTime)
              AND e.studentEntity.id = COALESCE(:studentId, e.studentEntity.id)
              AND e.employeeEntity.id = COALESCE(:employeeId, e.employeeEntity.id)
            """)
    Page<EventEntity> findAllWithFilter(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("studentId") UUID studentId,
            @Param("employeeId") UUID employeeId,
            Pageable pageable
    );

    Page<EventEntity> findAllByEmployeeEntityId(UUID employeeId, Pageable pageable);
    Page<EventEntity> findAllByStudentEntityId(UUID studentId, Pageable pageable);
}

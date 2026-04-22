package com.aprimorar.api.domain.event.repository;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;

import com.aprimorar.api.domain.event.Event;
import com.aprimorar.api.enums.EventStatus;

public final class EventSpecifications {

    private EventSpecifications() {
    }

    public static Specification<Event> searchContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
            if (term == null || term.trim().isEmpty()) {
                return null;
            }
            String pattern = "%" + term.toLowerCase() + "%";

            // Allow searching Enum values. Spring JPA doesn't natively do cb.like on enums,
            // so we cast to String.
            return cb.or(
                    cb.like(cb.lower(root.join("student").get("name")), pattern),
                    cb.like(cb.lower(root.join("employee").get("name")), pattern),
                    cb.like(cb.lower(root.get("content").as(String.class)), pattern)
            );
        };
    }

    public static Specification<Event> withStartDateAfter(Instant startDate) {
        return (root, query, cb) -> startDate == null ? null : cb.greaterThanOrEqualTo(root.get("startDate"), startDate);
    }

    public static Specification<Event> withEndDateBefore(Instant endDate) {
        return (root, query, cb) -> endDate == null ? null : cb.lessThanOrEqualTo(root.get("endDate"), endDate);
    }

    public static Specification<Event> withStatus(EventStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Event> withStudentId(java.util.UUID studentId) {
        return (root, query, cb) -> studentId == null ? null : cb.equal(root.join("student").get("id"), studentId);
    }

    public static Specification<Event> withEmployeeId(java.util.UUID employeeId) {
        return (root, query, cb) -> employeeId == null ? null : cb.equal(root.join("employee").get("id"), employeeId);
    }
}

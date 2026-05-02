package com.aprimorar.api.domain.event.repository;

import com.aprimorar.api.domain.event.Event;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

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

    public static Specification<Event> withStudentId(UUID studentId) {
        return (root, query, cb) -> studentId == null ? null : cb.equal(root.join("student").get("id"), studentId);
    }

    public static Specification<Event> withEmployeeId(UUID employeeId) {
        return (root, query, cb) -> employeeId == null ? null : cb.equal(root.join("employee").get("id"), employeeId);
    }

    public static Specification<Event> withStudentNameIgnoreCase(String term) {
        return (root, query, cb) -> {
            if (term == null || term.trim().isEmpty()) {
                return null;
            }
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.like(cb.lower(root.join("student").get("name")), pattern);
        };
    }

    public static Specification<Event> withEmployeePaid(Boolean paid) {
        return (root, query, cb) -> {
            if (paid == null) return null;
            return paid ? cb.isNotNull(root.get("employeePaymentDate")) : cb.isNull(root.get("employeePaymentDate"));
        };
    }

    public static Specification<Event> withStudentCharged(Boolean charged) {
        return (root, query, cb) -> {
            if (charged == null) return null;
            return charged ? cb.isNotNull(root.get("studentChargeDate")) : cb.isNull(root.get("studentChargeDate"));
        };
    }

    public static Specification<Event> withEmployeeNameIgnoreCase(String term) {
        return (root, query, cb) -> {
            if (term == null || term.trim().isEmpty()) {
                return null;
            }
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.like(cb.lower(root.join("employee").get("name")), pattern);
        };
    }
}

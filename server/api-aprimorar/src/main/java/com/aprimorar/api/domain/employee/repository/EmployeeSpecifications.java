package com.aprimorar.api.domain.employee.repository;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.enums.Duty;

public final class EmployeeSpecifications {
    private static final UUID GHOST_EMPLOYEE_ID = UUID.fromString("00000000-0000-4000-8000-000000000001");


    private EmployeeSpecifications() {
    }

    public static Specification<Employee> notArchived() {
        return (root, query, cb) -> cb.isNull(root.get("archivedAt"));
    }

    public static Specification<Employee> archived() {
        return (root, query, cb) -> cb.isNotNull(root.get("archivedAt"));
    }

    public static Specification<Employee> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("id"), GHOST_EMPLOYEE_ID);
    }

    public static Specification<Employee> searchContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.and(
                    cb.notEqual(root.get("duty"), Duty.SYSTEM),
                    cb.or(
                            cb.like(cb.lower(root.get("name")), pattern),
                            cb.like(cb.lower(root.get("email")), pattern),
                            cb.like(cb.lower(root.get("duty").as(String.class)), pattern)
                    )
            );
        };
    }
}

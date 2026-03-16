package com.aprimorar.api.domain.employee;

import org.springframework.data.jpa.domain.Specification;

public final class EmployeeSpecifications {

    private EmployeeSpecifications() {
    }

    public static Specification<Employee> notArchived() {
        return (root, query, cb) -> cb.isNull(root.get("archivedAt"));
    }

    public static Specification<Employee> searchContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("email")), pattern),
                cb.like(cb.lower(root.get("duty").as(String.class)), pattern)
            );
        };
    }
}

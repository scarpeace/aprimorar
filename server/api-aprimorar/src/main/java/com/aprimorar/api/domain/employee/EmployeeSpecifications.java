package com.aprimorar.api.domain.employee;

import org.springframework.data.jpa.domain.Specification;

public final class EmployeeSpecifications {

    private EmployeeSpecifications() {
    }

    public static Specification<Employee> notArchived() {
        return (root, query, cb) -> cb.isNull(root.get("archivedAt"));
    }
}

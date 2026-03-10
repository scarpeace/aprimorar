package com.aprimorar.api.domain.student;

import org.springframework.data.jpa.domain.Specification;

public final class StudentSpecifications {

    private StudentSpecifications() {
    }

    public static Specification<StudentEntity> nameContainsIgnoreCase(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<StudentEntity> notArchived() {
        return (root, query, cb) -> cb.isNull(root.get("archivedAt"));
    }
}

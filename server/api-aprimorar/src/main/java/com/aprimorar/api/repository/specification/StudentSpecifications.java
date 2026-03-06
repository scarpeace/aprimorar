package com.aprimorar.api.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.aprimorar.api.entity.Student;

public final class StudentSpecifications {

    private StudentSpecifications() {
    }

    public static Specification<Student> nameContainsIgnoreCase(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Student> notArchived() {
        return (root, query, cb) -> cb.isNull(root.get("archivedAt"));
    }
}

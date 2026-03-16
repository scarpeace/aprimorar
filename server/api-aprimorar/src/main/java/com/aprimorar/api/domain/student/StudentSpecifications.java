package com.aprimorar.api.domain.student;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public final class StudentSpecifications {

    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private StudentSpecifications() {
    }

    public static Specification<Student> nameContainsIgnoreCase(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Student> notArchived() {
        return (root, query, cb) -> cb.isNull(root.get("archivedAt"));
    }

    public static Specification<Student> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("id"), GHOST_STUDENT_ID);
    }
}

package com.aprimorar.api.domain.student.repository;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.aprimorar.api.domain.student.Student;

public final class StudentSpecifications {

    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private StudentSpecifications() {}

    public static Specification<Student> nameContainsIgnoreCase(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Student> searchContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
            String likeTerm = "%" + term.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), likeTerm),
                    cb.like(cb.lower(root.get("email")), likeTerm),
                    cb.like(cb.lower(root.get("school")), likeTerm),
                    cb.like(cb.lower(root.get("cpf")), likeTerm)
            );
        };
    }

    public static Specification<Student> belongsToParent(UUID parentId) {
        return (root, query, cb) -> cb.equal(root.get("parentId"), parentId);
    }

    public static Specification<Student> notArchived() {
        return (root, query, cb) -> cb.isNull(root.get("archivedAt"));
    }

    public static Specification<Student> archived() {
        return (root, query, cb) -> cb.isNotNull(root.get("archivedAt"));
    }

    public static Specification<Student> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("id"), GHOST_STUDENT_ID);
    }
}

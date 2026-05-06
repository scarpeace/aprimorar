package com.aprimorar.api.domain.parent.repository;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.aprimorar.api.domain.parent.Parent;

public final class ParentSpecifications {
    private static final UUID GHOST_PARENT_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");


    private ParentSpecifications() {}

    public static Specification<Parent> searchContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
            String likeTerm = "%" + term.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), likeTerm),
                    cb.like(cb.lower(root.get("email")), likeTerm),
                    cb.like(cb.lower(root.get("cpf")), likeTerm)
            );
        };
    }

    public static Specification<Parent> notArchived() {
        return (root, query, cb) -> cb.isNull(root.get("archivedAt"));
    }

    public static Specification<Parent> archived() {
        return (root, query, cb) -> cb.isNotNull(root.get("archivedAt"));
    }

    public static Specification<Parent> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("id"), GHOST_PARENT_ID);
    }
}

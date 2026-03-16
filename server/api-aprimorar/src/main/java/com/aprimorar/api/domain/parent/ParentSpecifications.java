package com.aprimorar.api.domain.parent;

import org.springframework.data.jpa.domain.Specification;

public final class ParentSpecifications {

    private ParentSpecifications() {
    }

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
}

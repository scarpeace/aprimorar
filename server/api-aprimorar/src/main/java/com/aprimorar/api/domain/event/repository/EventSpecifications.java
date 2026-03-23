package com.aprimorar.api.domain.event.repository;

import org.springframework.data.jpa.domain.Specification;

import com.aprimorar.api.domain.event.Event;

public final class EventSpecifications {

    private EventSpecifications() {
    }

    public static Specification<Event> searchContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
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
}

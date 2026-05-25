package aprimorar.pessoas.responsavel.internal.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.pessoas.responsavel.internal.domain.Responsavel;

public final class ResponsavelSpecifications {
    private static final UUID GHOST_PARENT_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");


    private ResponsavelSpecifications() {}

    public static Specification<Responsavel> searchContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
            String likeTerm = "%" + term.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), likeTerm),
                    cb.like(cb.lower(root.get("email")), likeTerm),
                    cb.like(cb.lower(root.get("cpf")), likeTerm)
            );
        };
    }

    public static Specification<Responsavel> isNotArchived() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<Responsavel> isArchived() {
        return (root, query, cb) -> cb.isFalse(root.get("active"));
    }

    public static Specification<Responsavel> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("id"), GHOST_PARENT_ID);
    }
}

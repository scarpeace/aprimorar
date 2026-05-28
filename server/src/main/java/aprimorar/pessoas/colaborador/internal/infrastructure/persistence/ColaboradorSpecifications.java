package aprimorar.pessoas.colaborador.internal.infrastructure.persistence;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.pessoas.colaborador.internal.domain.Colaborador;
import aprimorar.pessoas.colaborador.api.ColaboradorDutyEnum;

public final class ColaboradorSpecifications {
    private ColaboradorSpecifications() {
    }

    public static Specification<Colaborador> isArchived() {
        return (root, query, cb) -> cb.isFalse(root.get("active"));
    }

    public static Specification<Colaborador> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("duty"), ColaboradorDutyEnum.SYSTEM);
    }

    public static Specification<Colaborador> searchContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.and(
                    cb.notEqual(root.get("duty"), ColaboradorDutyEnum.SYSTEM),
                    cb.or(
                            cb.like(cb.lower(root.get("name")), pattern),
                            cb.like(cb.lower(root.get("email")), pattern),
                            cb.like(cb.lower(root.get("duty").as(String.class)), pattern)
                    )
            );
        };
    }
}

package aprimorar.pessoas.colaborador.internal.repository;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.pessoas.colaborador.internal.Colaborador;
import aprimorar.pessoas.colaborador.internal.ColaboradorDutyEnum;

public final class ColaboradorSpecifications {
    private ColaboradorSpecifications() {
    }

    public static Specification<Colaborador> isNotArchived() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
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

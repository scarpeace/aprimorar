package aprimorar.pessoas.aluno.infrastructure.persistence;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.pessoas.aluno.domain.Aluno;
import aprimorar.shared.enums.Role;

public final class AlunoSpecifications {

    private AlunoSpecifications() {}

    public static Specification<Aluno> searchContainsIgnoreCase(String term) {
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

    public static Specification<Aluno> isNotArchived() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<Aluno> isNotAdmin() {
        return (root, query, cb) -> cb.notEqual(root.get("role"), Role.ADMIN);
    }
}

package aprimorar.pessoas.aluno.internal.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.pessoas.aluno.internal.domain.Aluno;

public final class AlunoSpecifications {

    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private AlunoSpecifications() {}

    public static Specification<Aluno> nameContainsIgnoreCase(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

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

    public static Specification<Aluno> belongsToParent(UUID parentId) {
        return (root, query, cb) -> cb.equal(root.get("parentId"), parentId);
    }

    public static Specification<Aluno> isNotArchived() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<Aluno> isArchived() {
        return (root, query, cb) -> cb.isFalse(root.get("active"));
    }

    public static Specification<Aluno> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("id"), GHOST_STUDENT_ID);
    }
}

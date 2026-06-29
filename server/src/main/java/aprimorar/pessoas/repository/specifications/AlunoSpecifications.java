package aprimorar.pessoas.repository.specifications;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.dto.aluno.AlunoFiltroRequest;

public final class AlunoSpecifications {

    private AlunoSpecifications() {}

    public static Specification<Aluno> comFiltros(AlunoFiltroRequest filtro) {
        return Specification
            .where(nomeContem(filtro.nome()))
            .and(emailContem(filtro.email()))
            .and(cpfContem(filtro.cpf()))
            .and(escolaContem(filtro.escola()))
            .and(ativosContem(filtro.ativos()))
            .and(isNotGhost());
    }

    public static Specification<Aluno> nomeContem(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) return null;
            String pattern = "%" + nome.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("nome")), pattern);
        };
    }

    public static Specification<Aluno> emailContem(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) return null;
            String pattern = "%" + email.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("email")), pattern);
        };
    }

    public static Specification<Aluno> cpfContem(String cpf) {
        return (root, query, cb) -> {
            if (cpf == null || cpf.isBlank()) return null;
            String pattern = "%" + cpf.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("cpf")), pattern);
        };
    }

    public static Specification<Aluno> escolaContem(String escola) {
        return (root, query, cb) -> {
            if (escola == null || escola.isBlank()) return null;
            String pattern = "%" + escola.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("escola")), pattern);
        };
    }

    public static Specification<Aluno> ativosContem(Boolean ativos) {
        return (root, query, cb) -> {
            if (ativos == null) return null;
            return cb.equal(root.get("active"), ativos);
        };
    }

    public static Specification<Aluno> isNotArchived() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<Aluno> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("id"), java.util.UUID.fromString("00000000-0000-4000-8000-000000000002"));
    }
}

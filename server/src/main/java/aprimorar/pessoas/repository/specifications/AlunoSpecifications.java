package aprimorar.pessoas.repository.specifications;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.pessoas.domain.AlunoEntity;
import aprimorar.pessoas.web.dto.aluno.AlunoFiltroRequest;

public final class AlunoSpecifications {

    private AlunoSpecifications() {}

    public static Specification<AlunoEntity> comFiltros(AlunoFiltroRequest filtro) {
        return Specification
            .where(nomeContem(filtro.nome()))
            .and(emailContem(filtro.email()))
            .and(cpfContem(filtro.cpf()))
            .and(escolaContem(filtro.escola()))
            .and(ativosContem(filtro.ativos()));
    }

    public static Specification<AlunoEntity> nomeContem(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) return null;
            String pattern = "%" + nome.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("nome")), pattern);
        };
    }

    public static Specification<AlunoEntity> emailContem(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) return null;
            String pattern = "%" + email.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("email")), pattern);
        };
    }

    public static Specification<AlunoEntity> cpfContem(String cpf) {
        return (root, query, cb) -> {
            if (cpf == null || cpf.isBlank()) return null;
            String pattern = "%" + cpf.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("cpf")), pattern);
        };
    }

    public static Specification<AlunoEntity> escolaContem(String escola) {
        return (root, query, cb) -> {
            if (escola == null || escola.isBlank()) return null;
            String pattern = "%" + escola.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("escola")), pattern);
        };
    }

    public static Specification<AlunoEntity> ativosContem(Boolean ativos) {
        return (root, query, cb) -> {
            if (ativos == null) return null;
            return cb.equal(root.get("active"), ativos);
        };
    }

    public static Specification<AlunoEntity> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }
}

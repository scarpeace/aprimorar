package aprimorar.instituicao.alunos.repository;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.instituicao.alunos.domain.Aluno;
import aprimorar.instituicao.alunos.web.dto.aluno.AlunoFiltroRequest;

public final class AlunoSpecifications {

    private AlunoSpecifications() {}

    public static Specification<Aluno> comFiltros(AlunoFiltroRequest filtro) {
        return Specification
            .where(nomeContem(filtro.nome()))
            .and(emailContem(filtro.email()))
            .and(cpfContem(filtro.cpf()))
            .and(escolaContem(filtro.escola()))
            .and(ativosContem(filtro.ativos()));
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

    public static Specification<Aluno> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }
}

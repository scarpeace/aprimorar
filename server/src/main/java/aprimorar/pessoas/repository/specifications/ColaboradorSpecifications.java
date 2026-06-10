package aprimorar.pessoas.repository.specifications;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.dto.ColaboradorFiltroRequest;
import aprimorar.pessoas.shared.FuncoesColaborador;

public final class ColaboradorSpecifications {

    public static Specification<Colaborador> comFiltros(ColaboradorFiltroRequest filtro) {
        return Specification
            .where(nomeContem(filtro.nome()))
            .and(emailContem(filtro.email()))
            .and(cpfContem(filtro.cpf()))
            .and(ativosContem(filtro.ativos()))
            .and(isNotGhost());
    }

    public static Specification<Colaborador> nomeContem(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) return null;
            String pattern = "%" + nome.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("nome")), pattern);
        };
    }

    public static Specification<Colaborador> emailContem(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) return null;
            String pattern = "%" + email.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("email").get("value")), pattern);
        };
    }

    public static Specification<Colaborador> cpfContem(String cpf) {
        return (root, query, cb) -> {
            if (cpf == null || cpf.isBlank()) return null;
            String pattern = "%" + cpf.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("cpf").get("value")), pattern);
        };
    }

    public static Specification<Colaborador> ativosContem(Boolean ativos) {
        return (root, query, cb) -> {
            if (ativos == null) return null;
            return cb.equal(root.get("active"), ativos);
        };
    }

    public static Specification<Colaborador> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("funcao"), FuncoesColaborador.SISTEMA);
    }
}

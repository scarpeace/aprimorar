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
            .and(dutyContem(filtro.cpf()))
            .and(ativosContem(filtro.ativos()))
            .and(isNotGhost());
    }

    public static Specification<Colaborador> nomeContem(String term) {
        return (root, query, cb) -> {
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), pattern);
        };
    }

    public static Specification<Colaborador> emailContem(String term) {
        return (root, query, cb) -> {
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("email")), pattern);
        };
    }

    public static Specification<Colaborador> cpfContem(String term) {
        return (root, query, cb) -> {
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("cpf")), pattern);
        };
    }

    public static Specification<Colaborador> ativosContem(Boolean ativos) {
        return (root, query, cb) -> cb.equal(root.get("active"), ativos);
    }

    public static Specification<Colaborador> dutyContem(String term) {
        return (root, query, cb) -> {
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("duty").as(String.class)), pattern);
        };
    }

    public static Specification<Colaborador> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("duty"), FuncoesColaborador.SYSTEM);
    }
}

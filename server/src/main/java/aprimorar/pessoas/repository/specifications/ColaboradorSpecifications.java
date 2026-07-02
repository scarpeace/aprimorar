package aprimorar.pessoas.repository.specifications;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.dto.colaborador.ColaboradorFiltroRequest;

public final class ColaboradorSpecifications {

    private ColaboradorSpecifications() {}

    public static Specification<Colaborador> comFiltros(ColaboradorFiltroRequest filtro, UUID ghostColaboradorId) {
        return Specification
            .where(nomeContem(filtro.nome()))
            .and(emailContem(filtro.email()))
            .and(cpfContem(filtro.cpf()))
            .and(ativosContem(filtro.ativos()))
            .and(isNotGhost(ghostColaboradorId));
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
            return cb.like(cb.lower(root.get("email")), pattern);
        };
    }

    public static Specification<Colaborador> cpfContem(String cpf) {
        return (root, query, cb) -> {
            if (cpf == null || cpf.isBlank()) return null;
            String pattern = "%" + cpf.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("cpf")), pattern);
        };
    }

    public static Specification<Colaborador> ativosContem(Boolean ativos) {
        return (root, query, cb) -> {
            if (ativos == null) return null;
            return cb.equal(root.get("active"), ativos);
        };
    }

    public static Specification<Colaborador> isNotArchived() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<Colaborador> isNotGhost(UUID ghostColaboradorId) {
        return (root, query, cb) -> cb.notEqual(root.get("id"), ghostColaboradorId);
    }
}

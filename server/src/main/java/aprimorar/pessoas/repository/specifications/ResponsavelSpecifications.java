package aprimorar.pessoas.repository.specifications;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.dto.responsavel.ResponsavelFiltroRequest;


public final class ResponsavelSpecifications {
    private static final UUID GHOST_PARENT_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");


    private ResponsavelSpecifications() {}

    public static Specification<Responsavel> comFiltros(ResponsavelFiltroRequest filtro) {
        return Specification
            .where(nomeContem(filtro.nome()))
            .and(emailContem(filtro.email()))
            .and(cpfContem(filtro.cpf()))
            .and(isNotGhost());
    }

    public static Specification<Responsavel> nomeContem(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) return null;
            String pattern = "%" + nome.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("nome")), pattern);
        };
    }

    public static Specification<Responsavel> emailContem(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) return null;
            String pattern = "%" + email.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("email")), pattern);
        };
    }

    public static Specification<Responsavel> cpfContem(String cpf) {
        return (root, query, cb) -> {
            if (cpf == null || cpf.isBlank()) return null;
            String pattern = "%" + cpf.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("cpf")), pattern);
        };
    }

    public static Specification<Responsavel> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("id"), GHOST_PARENT_ID);
    }
}

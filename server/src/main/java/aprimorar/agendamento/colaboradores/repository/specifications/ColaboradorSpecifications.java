package aprimorar.agendamento.colaboradores.repository.specifications;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.agendamento.colaboradores.web.dto.colaborador.ColaboradorFiltroRequest;
import aprimorar.agendamento.colaboradores.domain.Colaborador;

public final class ColaboradorSpecifications {

    private ColaboradorSpecifications() {}

    public static Specification<Colaborador> comFiltros(ColaboradorFiltroRequest filtro) {
        return Specification.allOf(
            nomeContem(filtro.nome()),
            emailContem(filtro.email()),
            cpfContem(filtro.cpf()),
            ativosContem(filtro.ativos())
        );
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

    public static Specification<Colaborador> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }
}

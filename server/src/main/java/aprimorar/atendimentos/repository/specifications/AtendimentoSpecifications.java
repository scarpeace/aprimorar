package aprimorar.atendimentos.repository.specifications;

import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public final class AtendimentoSpecifications {

    private AtendimentoSpecifications() {
    }

    public static Specification<Atendimento> comFiltros(AtendimentoFiltroRequest filtro) {
        return Specification
            .where(buscaContem(filtro.busca()))
            .and(inicioMaiorOuIgual(filtro.inicio()))
            .and(fimMenorOuIgual(filtro.fim()))
            .and(statusIgual(filtro.status()))
            .and(tipoIgual(filtro.tipo()))
            .and(alunoIdIgual(filtro.alunoId()))
            .and(colaboradorIdIgual(filtro.colaboradorId()));
    }

    public static Specification<Atendimento> buscaContem(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + termo.trim().toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(root.get("tipo").as(String.class)), pattern),
                cb.like(cb.lower(root.get("nomeAluno")), pattern),
                cb.like(cb.lower(root.get("nomeColaborador")), pattern)
            );
        };
    }

    public static Specification<Atendimento> inicioMaiorOuIgual(Instant inicio) {
        return (root, query, cb) -> inicio == null ? null : cb.greaterThanOrEqualTo(root.get("inicio"), inicio);
    }

    public static Specification<Atendimento> fimMenorOuIgual(Instant fim) {
        return (root, query, cb) -> fim == null ? null : cb.lessThanOrEqualTo(root.get("fim"), fim);
    }

    public static Specification<Atendimento> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null ? null : cb.equal(root.get("alunoId"), alunoId);
    }

    public static Specification<Atendimento> statusIgual(StatusAtendimento status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Atendimento> tipoIgual(TipoAtendimento tipo) {
        return (root, query, cb) -> tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    public static Specification<Atendimento> colaboradorIdIgual(UUID colaboradorId) {
        return (root, query, cb) -> colaboradorId == null ? null : cb.equal(root.get("colaboradorId"), colaboradorId);
    }
}

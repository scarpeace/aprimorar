package aprimorar.atendimentos.repository.specifications;

import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.dto.AtendimentoFiltroRequest;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public final class AtendimentoSpecifications {

    private AtendimentoSpecifications() {
    }

    public static Specification<Atendimento> comFiltros(AtendimentoFiltroRequest filtro) {
        Boolean cobrado = Boolean.TRUE.equals(filtro.ocultarCobrados()) ? Boolean.FALSE : null;
        Boolean pago = Boolean.TRUE.equals(filtro.ocultarPagos()) ? Boolean.FALSE : null;

        return Specification
            .where(buscaContem(filtro.busca()))
            .and(inicioMaiorOuIgual(filtro.inicio()))
            .and(fimMenorOuIgual(filtro.fim()))
            .and(alunoCobradoContem(cobrado))
            .and(colaboradorPagoContem(pago));
    }

    public static Specification<Atendimento> buscaContem(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + termo.trim().toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(root.get("alunoNome")), pattern),
                cb.like(cb.lower(root.get("colaboradorNome")), pattern),
                cb.like(cb.lower(root.get("descricao")), pattern),
                cb.like(cb.lower(root.get("tipo").as(String.class)), pattern)
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

    public static Specification<Atendimento> colaboradorIdIgual(UUID colaboradorId) {
        return (root, query, cb) -> colaboradorId == null ? null : cb.equal(root.get("colaboradorId"), colaboradorId);
    }

    public static Specification<Atendimento> colaboradorPagoContem(Boolean pago) {
        return (root, query, cb) -> {
            if (pago == null) return null;
            return pago ? cb.isNotNull(root.get("dataPagamentoColaborador")) : cb.isNull(root.get("dataPagamentoColaborador"));
        };
    }

    public static Specification<Atendimento> alunoCobradoContem(Boolean cobrado) {
        return (root, query, cb) -> {
            if (cobrado == null) return null;
            return cobrado ? cb.isNotNull(root.get("dataCobrancaAluno")) : cb.isNull(root.get("dataCobrancaAluno"));
        };
    }

}

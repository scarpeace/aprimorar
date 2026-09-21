package aprimorar.financeiro.repasses_colaboradores.infrastructure;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses_colaboradores.domain.RepassePagamento;
import aprimorar.financeiro.repasses_colaboradores.web.dto.PagamentoFiltroRequest;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class PagamentoSpecifications {

    private PagamentoSpecifications() {
    }

    public static Specification<RepassePagamento> comFiltros(
        PagamentoFiltroRequest filtro
    ) {
        return Specification.allOf(
            colaboradorIdIgual(filtro.colaboradorId()),
            formaPagamentoIgual(filtro.formaPagamento()),
            dataPagamentoMaiorOuIgual(filtro.dataPagamentoInicio()),
            dataPagamentoMenorOuIgual(filtro.dataPagamentoFim())
        );
    }

    private static Specification<RepassePagamento> colaboradorIdIgual(
        UUID colaboradorId
    ) {
        return (root, query, cb) -> {
            if (colaboradorId == null) {
                return null;
            }

            query.distinct(true);
            return cb.equal(
                root.join("repasses").get("colaboradorId"),
                colaboradorId
            );
        };
    }

    private static Specification<RepassePagamento> formaPagamentoIgual(
        FormaPagamentoEnum formaPagamento
    ) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    private static Specification<RepassePagamento> dataPagamentoMaiorOuIgual(
        LocalDate dataPagamentoInicio
    ) {
        return (root, query, cb) -> dataPagamentoInicio == null
            ? null
            : cb.greaterThanOrEqualTo(
                root.get("dataPagamento"),
                dataPagamentoInicio
            );
    }

    private static Specification<RepassePagamento> dataPagamentoMenorOuIgual(
        LocalDate dataPagamentoFim
    ) {
        return (root, query, cb) -> dataPagamentoFim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataPagamento"), dataPagamentoFim);
    }
}

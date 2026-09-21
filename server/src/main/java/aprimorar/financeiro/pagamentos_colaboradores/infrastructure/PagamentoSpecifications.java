package aprimorar.financeiro.pagamentos_colaboradores.infrastructure;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.pagamentos_colaboradores.domain.Pagamento;
import aprimorar.financeiro.pagamentos_colaboradores.web.dto.PagamentoFiltroRequest;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class PagamentoSpecifications {

    private PagamentoSpecifications() {
    }

    public static Specification<Pagamento> comFiltros(
        PagamentoFiltroRequest filtro
    ) {
        return Specification.allOf(
            colaboradorIdIgual(filtro.colaboradorId()),
            formaPagamentoIgual(filtro.formaPagamento()),
            dataPagamentoMaiorOuIgual(filtro.dataPagamentoInicio()),
            dataPagamentoMenorOuIgual(filtro.dataPagamentoFim())
        );
    }

    private static Specification<Pagamento> colaboradorIdIgual(
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

    private static Specification<Pagamento> formaPagamentoIgual(
        FormaPagamentoEnum formaPagamento
    ) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    private static Specification<Pagamento> dataPagamentoMaiorOuIgual(
        LocalDate dataPagamentoInicio
    ) {
        return (root, query, cb) -> dataPagamentoInicio == null
            ? null
            : cb.greaterThanOrEqualTo(
                root.get("dataPagamento"),
                dataPagamentoInicio
            );
    }

    private static Specification<Pagamento> dataPagamentoMenorOuIgual(
        LocalDate dataPagamentoFim
    ) {
        return (root, query, cb) -> dataPagamentoFim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataPagamento"), dataPagamentoFim);
    }
}

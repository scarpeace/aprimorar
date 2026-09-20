package aprimorar.financeiro.pagamentos_particular.repository;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.pagamentos_particular.domain.PagamentoParticular;
import aprimorar.financeiro.pagamentos_particular.web.dto.PagamentoParticularFiltroRequest;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class PagamentoParticularSpecifications {

    private PagamentoParticularSpecifications() {
    }

    public static Specification<PagamentoParticular> comFiltros(
        PagamentoParticularFiltroRequest filtro
    ) {
        return Specification.allOf(
            colaboradorIdIgual(filtro.colaboradorId()),
            formaPagamentoIgual(filtro.formaPagamento()),
            dataPagamentoMaiorOuIgual(filtro.dataPagamentoInicio()),
            dataPagamentoMenorOuIgual(filtro.dataPagamentoFim())
        );
    }

    private static Specification<PagamentoParticular> colaboradorIdIgual(
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

    private static Specification<PagamentoParticular> formaPagamentoIgual(
        FormaPagamentoEnum formaPagamento
    ) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    private static Specification<PagamentoParticular> dataPagamentoMaiorOuIgual(
        LocalDate dataPagamentoInicio
    ) {
        return (root, query, cb) -> dataPagamentoInicio == null
            ? null
            : cb.greaterThanOrEqualTo(
                root.get("dataPagamento"),
                dataPagamentoInicio
            );
    }

    private static Specification<PagamentoParticular> dataPagamentoMenorOuIgual(
        LocalDate dataPagamentoFim
    ) {
        return (root, query, cb) -> dataPagamentoFim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataPagamento"), dataPagamentoFim);
    }
}

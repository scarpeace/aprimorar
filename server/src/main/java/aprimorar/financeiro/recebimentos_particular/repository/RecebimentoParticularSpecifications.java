package aprimorar.financeiro.recebimentos_particular.repository;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.recebimentos_particular.domain.RecebimentoParticular;
import aprimorar.financeiro.recebimentos_particular.web.dto.RecebimentoParticularFiltroRequest;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class RecebimentoParticularSpecifications {

    private RecebimentoParticularSpecifications() {
    }

    public static Specification<RecebimentoParticular> comFiltros(
        RecebimentoParticularFiltroRequest filtro
    ) {
        return Specification.allOf(
            alunoIdIgual(filtro.alunoId()),
            formaPagamentoIgual(filtro.formaPagamento()),
            dataRecebimentoMaiorOuIgual(filtro.dataRecebimentoInicio()),
            dataRecebimentoMenorOuIgual(filtro.dataRecebimentoFim())
        );
    }

    private static Specification<RecebimentoParticular> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> {
            if (alunoId == null) {
                return null;
            }

            query.distinct(true);
            return cb.equal(root.join("cobrancas").get("alunoId"), alunoId);
        };
    }

    private static Specification<RecebimentoParticular> formaPagamentoIgual(
        FormaPagamentoEnum formaPagamento
    ) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    private static Specification<RecebimentoParticular> dataRecebimentoMaiorOuIgual(
        LocalDate dataRecebimentoInicio
    ) {
        return (root, query, cb) -> dataRecebimentoInicio == null
            ? null
            : cb.greaterThanOrEqualTo(
                root.get("dataRecebimento"),
                dataRecebimentoInicio
            );
    }

    private static Specification<RecebimentoParticular> dataRecebimentoMenorOuIgual(
        LocalDate dataRecebimentoFim
    ) {
        return (root, query, cb) -> dataRecebimentoFim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataRecebimento"), dataRecebimentoFim);
    }
}

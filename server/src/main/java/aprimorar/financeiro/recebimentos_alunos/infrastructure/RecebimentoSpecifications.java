package aprimorar.financeiro.recebimentos_alunos.infrastructure;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.recebimentos_alunos.domain.CobrancaRecebimento;
import aprimorar.financeiro.recebimentos_alunos.web.dto.RecebimentoFiltroRequest;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class RecebimentoSpecifications {

    private RecebimentoSpecifications() {
    }

    public static Specification<CobrancaRecebimento> comFiltros(
        RecebimentoFiltroRequest filtro
    ) {
        return Specification.allOf(
            alunoIdIgual(filtro.alunoId()),
            formaPagamentoIgual(filtro.formaPagamento()),
            dataRecebimentoMaiorOuIgual(filtro.dataRecebimentoInicio()),
            dataRecebimentoMenorOuIgual(filtro.dataRecebimentoFim())
        );
    }

    private static Specification<CobrancaRecebimento> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> {
            if (alunoId == null) {
                return null;
            }

            query.distinct(true);
            return cb.equal(root.join("cobrancas").get("alunoId"), alunoId);
        };
    }

    private static Specification<CobrancaRecebimento> formaPagamentoIgual(
        FormaPagamentoEnum formaPagamento
    ) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    private static Specification<CobrancaRecebimento> dataRecebimentoMaiorOuIgual(
        LocalDate dataRecebimentoInicio
    ) {
        return (root, query, cb) -> dataRecebimentoInicio == null
            ? null
            : cb.greaterThanOrEqualTo(
                root.get("dataRecebimento"),
                dataRecebimentoInicio
            );
    }

    private static Specification<CobrancaRecebimento> dataRecebimentoMenorOuIgual(
        LocalDate dataRecebimentoFim
    ) {
        return (root, query, cb) -> dataRecebimentoFim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataRecebimento"), dataRecebimentoFim);
    }
}

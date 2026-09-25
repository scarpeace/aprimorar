package aprimorar.financeiro.recebimentos_alunos.infrastructure;

import aprimorar.financeiro.recebimentos_alunos.domain.Cobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.enums.StatusCobranca;
import aprimorar.financeiro.recebimentos_alunos.web.dto.CobrancaFiltroRequest;
import aprimorar.common.FormaPagamentoEnum;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class CobrancaSpecifications {

    private CobrancaSpecifications() {
    }

    public static Specification<Cobranca> comFiltros(
        CobrancaFiltroRequest filtro
    ) {
        return Specification.allOf(
            alunoIdIgual(filtro.alunoId()),
            statusEm(filtro.status()),
            formaPagamentoIgual(filtro.formaPagamento()),
            dataRecebimentoMaiorOuIgual(filtro.dataRecebimentoInicio()),
            dataRecebimentoMenorOuIgual(filtro.dataRecebimentoFim())
        );
    }

    private static Specification<Cobranca> statusEm(
        List<StatusCobranca> statuses
    ) {
        return (root, query, cb) -> statuses == null || statuses.isEmpty()
            ? null
            : root.get("status").in(statuses);
    }

    private static Specification<Cobranca> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null
            ? null
            : cb.equal(root.get("alunoId"), alunoId);
    }

    private static Specification<Cobranca> formaPagamentoIgual(
        FormaPagamentoEnum formaPagamento
    ) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.join("recebimento").get("formaPagamento"), formaPagamento);
    }

    private static Specification<Cobranca> dataRecebimentoMaiorOuIgual(
        LocalDate dataRecebimentoInicio
    ) {
        return (root, query, cb) -> dataRecebimentoInicio == null
            ? null
            : cb.greaterThanOrEqualTo(
                root.join("recebimento").get("dataRecebimento"),
                dataRecebimentoInicio
            );
    }

    private static Specification<Cobranca> dataRecebimentoMenorOuIgual(
        LocalDate dataRecebimentoFim
    ) {
        return (root, query, cb) -> dataRecebimentoFim == null
            ? null
            : cb.lessThanOrEqualTo(
                root.join("recebimento").get("dataRecebimento"),
                dataRecebimentoFim
            );
    }
}

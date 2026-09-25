package aprimorar.financeiro.repasses_colaboradores.infrastructure;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses_colaboradores.domain.Repasse;
import aprimorar.financeiro.repasses_colaboradores.domain.enums.StatusRepasse;
import aprimorar.financeiro.repasses_colaboradores.web.dto.RepasseFiltroRequest;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class RepasseSpecifications {

    private RepasseSpecifications() {
    }

    public static Specification<Repasse> comFiltros(
        RepasseFiltroRequest filtro
    ) {
        return Specification.allOf(
            colaboradorIdIgual(filtro.colaboradorId()),
            statusIgual(filtro.status()),
            formaPagamentoIgual(filtro.formaPagamento()),
            dataPagamentoMaiorOuIgual(filtro.dataPagamentoInicio()),
            dataPagamentoMenorOuIgual(filtro.dataPagamentoFim())
        );
    }

    private static Specification<Repasse> colaboradorIdIgual(
        UUID colaboradorId
    ) {
        return (root, query, cb) -> colaboradorId == null
            ? null
            : cb.equal(root.get("colaboradorId"), colaboradorId);
    }

    private static Specification<Repasse> statusIgual(
        StatusRepasse status
    ) {
        return (root, query, cb) -> status == null
            ? null
            : cb.equal(root.get("status"), status);
    }

    private static Specification<Repasse> formaPagamentoIgual(
        FormaPagamentoEnum formaPagamento
    ) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.join("pagamento").get("formaPagamento"), formaPagamento);
    }

    private static Specification<Repasse> dataPagamentoMaiorOuIgual(
        LocalDate dataPagamentoInicio
    ) {
        return (root, query, cb) -> dataPagamentoInicio == null
            ? null
            : cb.greaterThanOrEqualTo(
                root.join("pagamento").get("dataPagamento"),
                dataPagamentoInicio
            );
    }

    private static Specification<Repasse> dataPagamentoMenorOuIgual(
        LocalDate dataPagamentoFim
    ) {
        return (root, query, cb) -> dataPagamentoFim == null
            ? null
            : cb.lessThanOrEqualTo(
                root.join("pagamento").get("dataPagamento"),
                dataPagamentoFim
            );
    }
}

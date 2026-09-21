package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.repository;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.RepasseParticular;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.enums.StatusRepasseParticular;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web.dto.RepasseParticularFiltroRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class RepasseParticularSpecifications {

    private RepasseParticularSpecifications() {
    }

    public static Specification<RepasseParticular> comFiltros(
        RepasseParticularFiltroRequest filtro
    ) {
        return Specification.allOf(
            colaboradorIdIgual(filtro.colaboradorId()),
            statusIgual(filtro.status()),
            formaPagamentoIgual(filtro.formaPagamento()),
            dataPagamentoMaiorOuIgual(filtro.dataPagamentoInicio()),
            dataPagamentoMenorOuIgual(filtro.dataPagamentoFim())
        );
    }

    private static Specification<RepasseParticular> colaboradorIdIgual(
        UUID colaboradorId
    ) {
        return (root, query, cb) -> colaboradorId == null
            ? null
            : cb.equal(root.get("colaboradorId"), colaboradorId);
    }

    private static Specification<RepasseParticular> statusIgual(
        StatusRepasseParticular status
    ) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }

            if (status == StatusRepasseParticular.ATRASADO) {
                return cb.and(
                    cb.equal(root.get("status"), StatusRepasseParticular.PENDENTE),
                    cb.lessThan(
                        root.get("createdAt"),
                        LocalDateTime.now().minusDays(30)
                    ),
                    cb.isNull(root.get("pagamento"))
                );
            }

            return cb.equal(root.get("status"), status);
        };
    }

    private static Specification<RepasseParticular> formaPagamentoIgual(
        FormaPagamentoEnum formaPagamento
    ) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.join("pagamento").get("formaPagamento"), formaPagamento);
    }

    private static Specification<RepasseParticular> dataPagamentoMaiorOuIgual(
        LocalDate dataPagamentoInicio
    ) {
        return (root, query, cb) -> dataPagamentoInicio == null
            ? null
            : cb.greaterThanOrEqualTo(
                root.join("pagamento").get("dataPagamento"),
                dataPagamentoInicio
            );
    }

    private static Specification<RepasseParticular> dataPagamentoMenorOuIgual(
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

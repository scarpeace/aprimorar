package aprimorar.financeiro.repasses.repository.specifications;

import aprimorar.financeiro.repasses.domain.enums.StatusRepasseIndividual;
import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses.domain.RepasseIndividualEntity;
import aprimorar.financeiro.repasses.web.dto.RepasseIndividualFiltroRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class RepasseIndividualSpecifications {

    private RepasseIndividualSpecifications() {
    }

    public static Specification<RepasseIndividualEntity> comFiltros(
        RepasseIndividualFiltroRequest filtro
    ) {
        return Specification
            .where(colaboradorIdIgual(filtro.colaboradorId()))
            .and(statusIgual(filtro.status()))
            .and(formaPagamentoIgual(filtro.formaPagamento()))
            .and(dataRepasseMaiorOuIgual(filtro.dataRepasseInicio()))
            .and(dataRepasseMenorOuIgual(filtro.dataRepasseFim()));
    }

    private static Specification<RepasseIndividualEntity> colaboradorIdIgual(UUID colaboradorId) {
        return (root, query, cb) -> colaboradorId == null ? null : cb.equal(root.get("colaboradorId"), colaboradorId);
    }

    private static Specification<RepasseIndividualEntity> statusIgual(StatusRepasseIndividual status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    private static Specification<RepasseIndividualEntity> formaPagamentoIgual(FormaPagamentoEnum formaPagamento) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    private static Specification<RepasseIndividualEntity> dataRepasseMaiorOuIgual(LocalDate data) {
        return (root, query, cb) -> data == null
            ? null
            : cb.greaterThanOrEqualTo(root.<LocalDateTime>get("dataRepasse"), data.atStartOfDay());
    }

    private static Specification<RepasseIndividualEntity> dataRepasseMenorOuIgual(LocalDate data) {
        return (root, query, cb) -> data == null
            ? null
            : cb.lessThanOrEqualTo(root.<LocalDateTime>get("dataRepasse"), data.atTime(LocalTime.MAX));
    }

}
